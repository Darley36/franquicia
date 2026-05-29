package com.co.bancolombia.franchise.domain.ports.out;

import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> saveFranchise(Franchise franchise);
    Mono<Franchise> findByName(String name);
    Mono<Branch> addBranchToFranchise(String franchiseId, Branch branch);
    Mono<Product> addProductToBranch(String franchiseId, String branchId, Product product);
    Flux<Franchise> findAll();
}
