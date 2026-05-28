package com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.mapper;

import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.model.Product;
import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.entity.BranchData;
import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.entity.FranchiseData;
import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.entity.ProductData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FranchiseMapperPercistence {

    private FranchiseMapperPercistence() {}

    public static Franchise toDomain(FranchiseData data) {
        return Franchise.builder()
                .id(data.getId())
                .name(data.getName())
                .branches(toBranchDomainList(data.getBranches()))
                .build();
    }

    public static FranchiseData toData(Franchise entity) {
        return FranchiseData.builder()
                .id(entity.getId())
                .name(entity.getName())
                .branches(toBranchEntityList(entity.getBranches()))
                .build();
    }

    public static Branch toDomain(BranchData entity) {
        if (entity == null) {
            return null;
        }
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .products(toProductDomainList(entity.getProducts()))
                .build();
    }

    public static BranchData toData(Branch branch) {
        if (branch == null) {
            return null;
        }
        return BranchData.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(toProductEntityList(branch.getProducts()))
                .build();
    }

    public static Product toDomain(ProductData entity) {
        if (entity == null) {
            return null;
        }
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock(entity.getStock())
                .build();
    }

    public static ProductData toData(Product product) {
        if (product == null) {
            return null;
        }
        return ProductData.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }

    private static List<Branch> toBranchDomainList(List<BranchData> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(FranchiseMapperPercistence::toDomain)
                .collect(Collectors.toList());
    }

    private static List<BranchData> toBranchEntityList(List<Branch> branches) {
        if (branches == null || branches.isEmpty()) {
            return Collections.emptyList();
        }
        return branches.stream()
                .map(FranchiseMapperPercistence::toData)
                .collect(Collectors.toList());
    }

    private static List<Product> toProductDomainList(List<ProductData> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(FranchiseMapperPercistence::toDomain)
                .collect(Collectors.toList());
    }

    private static List<ProductData> toProductEntityList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptyList();
        }
        return products.stream()
                .map(FranchiseMapperPercistence::toData)
                .collect(Collectors.toList());
    }

}
