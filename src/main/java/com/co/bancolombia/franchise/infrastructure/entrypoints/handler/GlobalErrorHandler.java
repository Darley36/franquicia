package com.co.bancolombia.franchise.infrastructure.entrypoints.handler;

import com.co.bancolombia.franchise.domain.exceptions.ApplicationException;
import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.domain.exceptions.ValidationException;
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
        return switch (error) {
            case ValidationException validationException -> handleExceptionWithHttpStatus(validationException, validationException.getHttpStatus(), request, "Field validation error");
            case BusinessException businessException -> handleExceptionWithHttpStatus(businessException, businessException.getHttpStatus(), request, "Business error");
            default -> handleExceptionWithHttpStatus(error, HttpStatus.INTERNAL_SERVER_ERROR, request, "Technical error");
        };
    }

    private Mono<ServerResponse> handleExceptionWithHttpStatus(Throwable ex, HttpStatus httpStatus, ServerRequest request, String errorType) {
        String logLevel = httpStatus.is5xxServerError() ? "error" : "warn";
        String logMessage = String.format("%s en %s: %s", errorType, request.path(), ex.getMessage());

        if ("error".equals(logLevel)) {
            log.error(logMessage, ex);
        } else {
            log.warn(logMessage);
        }

        GlobalResponse globalResponse = GlobalResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.path())
                .build();

        return ServerResponse
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(globalResponse);
    }
}
