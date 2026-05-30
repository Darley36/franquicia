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
@Schema(description = "Top product with highest stock per branch")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopProductPerBranchResponseDto {

    @Schema(description = "Branch ID")
    private String branchId;

    @Schema(description = "Branch name")
    private String branchName;

    @Schema(description = "Product with highest stock in this branch")
    private ProductResponseDto topProduct;
}

