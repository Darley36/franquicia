package com.co.bancolombia.franchise.infrastructure.entrypoints.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information about a product")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDto {
    @Schema(description = "Product ID")
    private String id;

    @Schema(description = "Product name")
    private String name;

    @Schema(description = "Product stock quantity")
    private Integer stock;
}

