package com.co.bancolombia.franchise.infrastructure.entrypoints.handler;

import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.infrastructure.entrypoints.dto.GlobalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
public class GlobalErrorHandler {

    public Mono<ServerResponse> handlerError(Throwable error, ServerRequest request) {
        log.error("Error procesando request a: {} - Error: {}", request.path(), error.getMessage(), error);

        return switch (error) {
            case BusinessException businessException -> handlerBusinessException(businessException, request);
            default -> handlerGenericException(request);
        };
    }

    private Mono<ServerResponse> handlerBusinessException(BusinessException ex, ServerRequest request) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.path())
                .build();

        return ServerResponse
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(globalResponse);
    }

    private Mono<ServerResponse> handlerGenericException(ServerRequest request) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .message("Ha ocurrido un error procesando la solicitud")
                .timestamp(LocalDateTime.now())
                .path(request.path())
                .build();

        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(globalResponse);
    }
}
