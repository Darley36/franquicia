package com.co.bancolombia.franchise.domain.ports.out;

import com.co.bancolombia.franchise.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> saveFranchise(Franchise franchise);
    Mono<Franchise> findByName(String name);
}
