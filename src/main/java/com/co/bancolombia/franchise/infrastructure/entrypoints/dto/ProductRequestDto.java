package com.co.bancolombia.franchise.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    private String name;

    private Integer stock;
}
