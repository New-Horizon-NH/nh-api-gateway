package com.newhorizon.nhapigateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newhorizon.nhapigateway.monolith.BaseResponse;
import com.newhorizon.nhapigateway.monolith.ResponseCodesEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@Slf4j
public class AuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizationGatewayFilterFactory.Config> implements Ordered {

    private final ObjectMapper objectMapper;

    public AuthorizationGatewayFilterFactory(ObjectMapper objectMapper) {
        super(Config.class);
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = extractToken(exchange.getRequest().getHeaders());

            if (isNull(token)) {
                // Create an unauthorized response
                return handleUnauthorizedResponse(exchange);
            } else {
                // Continue the filter chain if token is present
                return chain.filter(exchange);
            }
        };
    }

    // Reactive handling for unauthorized response
    private Mono<Void> handleUnauthorizedResponse(ServerWebExchange exchange) {
        BaseResponse.BaseResponseBuilder<?, ?> response = BaseResponse.builder()
                .status(BaseResponse.Status.FAILURE)
                .responseCode(ResponseCodesEnum.UNAUTHORIZED.getErrorCode())
                .responseMessage(ResponseCodesEnum.UNAUTHORIZED.getDescription());

        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.setStatusCode(ResponseCodesEnum.UNAUTHORIZED.getHttpErrorCode());
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Write the response body as JSON
        return serverHttpResponse.writeWith(
                Mono.fromSupplier(() -> {
                    DataBuffer buffer = getResponseBuffer(exchange, response.build());
                    return buffer;
                })
        );
    }

    @SneakyThrows
    private DataBuffer getResponseBuffer(ServerWebExchange exchange, Object response) {
        byte[] bytes = objectMapper.writeValueAsString(response).getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().bufferFactory().wrap(bytes);
    }

    // Token extraction remains the same
    private String extractToken(HttpHeaders headers) {
        return headers.containsKey(HttpHeaders.AUTHORIZATION) && nonNull(headers.getFirst(HttpHeaders.AUTHORIZATION)) ?
                headers.getFirst(HttpHeaders.AUTHORIZATION).replaceAll("[Bb]earer ", "") : null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class Config {
        // Config class can hold custom properties for the filter if needed
    }
}
