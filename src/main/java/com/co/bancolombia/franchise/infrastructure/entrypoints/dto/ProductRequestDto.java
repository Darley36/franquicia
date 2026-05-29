package com.co.bancolombia.franchise.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information for creating a new product")
public class ProductRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Stock is required")
    private Integer stock;
}
