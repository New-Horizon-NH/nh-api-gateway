package com.newhorizon.nhapigateway.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newhorizon.nhapigateway.dto.RouteDTO;
import com.newhorizon.nhapigateway.enums.RouteRetentionEnum;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class RouteHelperImplV1 implements RouteHelper {
    private final RouteLocator routeLocator;
    private final ObjectMapper objectMapper;
    private final Pattern pathPattern = Pattern.compile("Paths:\\s\\[.*?\\]");
    private final Pattern methodPattern = Pattern.compile("Methods:\\s\\[.*?\\]");

    @Override
    public List<String> getRoutesWithRetention(HttpMethod httpMethod,
                                               @NonNull RouteRetentionEnum retentionEnum) {
        checkArgument(retentionEnum);
        List<String> routes = new ArrayList<>();
        List<RouteDTO> routeVMList;
        if (isNull(httpMethod)) {
            routeVMList = getRoutes().stream()
                    .filter(r -> retentionEnum.getRetention()
                            .equals(r.getMetadata().getRetention()))
                    .toList();
        } else {
            routeVMList = getRoutes().stream()
                    .filter(r -> retentionEnum.getRetention()
                            .equals(r.getMetadata().getRetention()) &&
                            r.getHttpMethods().contains(httpMethod.toString().toUpperCase()))
                    .toList();
        }
        routeVMList.forEach(r -> routes.add(r.getPath()));
        return routes;
    }

    @Override
    public List<RouteDTO> getRoutes() {
        Flux<Route> routes = routeLocator.getRoutes();
        List<RouteDTO> routeVMs = new ArrayList<>();
        AtomicReference<Matcher> pathMatcher = new AtomicReference<>();
        AtomicReference<Matcher> methodMatcher = new AtomicReference<>();
        routes.subscribe(route -> {
            pathMatcher.set(pathPattern.matcher(route.getPredicate().toString()));
            methodMatcher.set(methodPattern.matcher(route.getPredicate().toString()));
            if (pathMatcher.get().find()) {
                routeVMs.add(RouteDTO.builder()
                        .path(pathMatcher.get()
                                .group(0)
                                .replaceAll(".*\\[", "")
                                .replaceAll("].*", ""))
                        .httpMethods(methodMatcher.get().find() ?
                                Arrays.stream(methodMatcher.get()
                                                .group(0)
                                                .replaceAll(".*\\[", "")
                                                .replaceAll("].*", "")
                                                .split(","))
                                        .toList()
                                        .stream().map(String::trim)
                                        .map(String::toUpperCase)
                                        .toList() :
                                getAllHttpMethod())
                        .routeId(route.getId())
                        .metadata(objectMapper.convertValue(route.getMetadata(), RouteDTO.RouteMetadata.class))
                        .build());
            }
        });
        return routeVMs;
    }

    public List<String> getAllHttpMethod() {
        List<String> httpMethods = new ArrayList<>();
        Arrays.stream(HttpMethod.values())
                .forEach(m -> httpMethods.add(m.name().toUpperCase()));
        return httpMethods;
    }

    @Override
    public void logRoutesWithRetention(RouteRetentionEnum retentionEnum) {
        List<String> routes = getRoutesWithRetention(null, retentionEnum);
        routes.sort(Comparator.naturalOrder());
        log.info("\n".concat(retentionEnum.getRetention().toUpperCase())
                .concat(" Routes:\n")
                .concat(String.join("\n", routes)));
    }

    public void logCompleteRoutesWithRetention(RouteRetentionEnum retentionEnum){
        getRoutes().stream()
                .filter(r->r.getMetadata().getRetention().equals(retentionEnum.getRetention()))
                .forEach(r->log.info(String.valueOf(r)));
    }

    @PostConstruct
    void init() {
        logCompleteRoutesWithRetention(RouteRetentionEnum.PUBLIC);
        logCompleteRoutesWithRetention(RouteRetentionEnum.PRIVATE);
    }

}
