package com.co.bancolombia.franchise.infrastructure.entrypoints.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information about franchise")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FranchiseResponseDto {
    @Schema(description = "Franchise ID")
    private String id;

    @Schema(description = "Franchise name")
    private String name;

    @Schema(description = "List of branches in this franchise")
    private List<BranchResponseDto> branches;
}
