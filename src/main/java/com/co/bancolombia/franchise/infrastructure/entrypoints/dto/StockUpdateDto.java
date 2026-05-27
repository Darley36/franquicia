package com.co.bancolombia.franchise.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload to update product stock")
public class StockUpdateDto {
    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be >= 0")
    private Integer stock;
}
