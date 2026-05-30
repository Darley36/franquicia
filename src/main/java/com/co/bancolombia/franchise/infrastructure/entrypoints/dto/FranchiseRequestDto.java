package com.co.bancolombia.franchise.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information for creating a new franchise")
public class FranchiseRequestDto {
    @NotBlank(message = "Franchise name cannot be empty")
    @Size(min = 1, max = 255, message = "Franchise name must be between 1 and 255 characters")
    private String name;
}
