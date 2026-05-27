package com.co.bancolombia.franchise.infrastructure.entrypoints.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalResponse {
    //@Schema(description = "Error code")
    //private String code;

    @Schema(description = "Descriptive error message")
    private String message;

    @Schema(description = "Date and time of the error")
    private LocalDateTime timestamp;

    @Schema(description = "Path to the endpoint where the error occurred")
    private String path;
}
