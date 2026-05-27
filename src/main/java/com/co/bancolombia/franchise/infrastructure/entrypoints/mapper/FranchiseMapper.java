package com.co.bancolombia.franchise.infrastructure.entrypoints.mapper;

import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.model.Product;
import com.co.bancolombia.franchise.infrastructure.entrypoints.dto.BranchRequestDto;
import com.co.bancolombia.franchise.infrastructure.entrypoints.dto.FranchiseRequestDto;
import com.co.bancolombia.franchise.infrastructure.entrypoints.dto.ProductRequestDto;

public class FranchiseMapper {

    private FranchiseMapper() {}

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

    public static Product toDomain(ProductRequestDto dto) {
        return Product.builder()
                .name(dto.getName())
                .stock(dto.getStock() != null ? dto.getStock().toString() : null)
                .build();
    }
}
