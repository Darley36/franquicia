package com.co.bancolombia.franchise.infrastructure.entrypoints.mapper;

import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.model.Product;
import com.co.bancolombia.franchise.domain.model.TopProductPerBranch;
import com.co.bancolombia.franchise.infrastructure.entrypoints.dto.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FranchiseMapper {

    private FranchiseMapper() {}

    // ===== Request DTOs to Domain =====

    public static Franchise toDomain(FranchiseRequestDto dto) {
        return Franchise.builder()
                .name(dto.getName())
                .build();
    }

    public static Branch toDomain(BranchRequestDto dto) {
        return Branch.builder()
                .name(dto.getName())
                .build();
    }

    public static Product toDomain( ProductRequestDto dto) {
        return Product.builder()
                .name(dto.getName())
                .stock(dto.getStock() != null ? dto.getStock() : null)
                .build();
    }

    // ===== Domain to Response DTOs =====

    public static FranchiseResponseDto toResponseDto(Franchise franchise) {
        if (franchise == null) {
            return null;
        }

        return FranchiseResponseDto.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(toBranchResponseDtoList(franchise.getBranches()))
                .build();
    }

    public static BranchResponseDto toResponseDto(Branch branch) {
        if (branch == null) {
            return null;
        }

        return BranchResponseDto.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(toProductResponseDtoList(branch.getProducts()))
                .build();
    }

    public static ProductResponseDto toResponseDto(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }

    public static TopProductPerBranchResponseDto toResponseDto(TopProductPerBranch topProductPerBranch) {
        if (topProductPerBranch == null) {
            return null;
        }

        return TopProductPerBranchResponseDto.builder()
                .branchName(topProductPerBranch.getBranchName())
                .topProduct(toResponseDto(topProductPerBranch.getTopProduct()))
                .build();
    }

    // ===== List Converters =====

    public static List<BranchResponseDto> toBranchResponseDtoList(List<Branch> branches) {
        if (branches == null || branches.isEmpty()) {
            return Collections.emptyList();
        }
        return branches.stream()
                .map(FranchiseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public static List<ProductResponseDto> toProductResponseDtoList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }
        return products.stream()
                .map(FranchiseMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
