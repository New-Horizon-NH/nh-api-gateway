package com.newhorizon.nhapigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
@Slf4j
public class PreLastPostGlobalFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        Set<URI> uris = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
        String originalUri = (uris.isEmpty()) ? "Unknown" : uris.iterator().next().toString();
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String incomingLogString = MessageFormat.format("[INCOMING REQUEST] : OriginalUri => {0}, HttpMethod => {1}, RouteId => {2}, FinalUri => {3} ",
                originalUri,
                exchange.getRequest().getMethod(),
                route.getId(),
                routeUri);
        log.info(incomingLogString);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
//                    String outgoingLogString=MessageFormat.format("[OUTGOING RESPONSE] : Body => {0}", exchange.toString());
//                    log.info(outgoingLogString);
                }));
    }

    // Set the order of this filter; lower value has higher precedence
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}