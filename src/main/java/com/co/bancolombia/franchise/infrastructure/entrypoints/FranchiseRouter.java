package com.co.bancolombia.franchise.infrastructure.entrypoints;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
public class FranchiseRouter {

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes() {

        private final FranchiseHandler handler;

        return route()
                //  Crear Franquicia
                .POST("/api/franchises", handler::())
                .build();
    }
}
