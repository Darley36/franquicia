package com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.mapper;

import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.model.Product;
import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.entity.FranchiseData;
import com.co.bancolombia.franchise.infrastructure.entrypoints.dto.BranchRequestDto;
import com.co.bancolombia.franchise.infrastructure.entrypoints.dto.FranchiseRequestDto;
import com.co.bancolombia.franchise.infrastructure.entrypoints.dto.ProductRequestDto;

public class FranchiseMapperPercistence {

    private FranchiseMapperPercistence() {}

    public static Franchise toDomain(FranchiseData data) {
        return Franchise.builder()
                .id(data.getId())
                .name(data.getName())
                .build();
    }

    public static FranchiseData toData(Franchise entity) {
        return FranchiseData.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

}
