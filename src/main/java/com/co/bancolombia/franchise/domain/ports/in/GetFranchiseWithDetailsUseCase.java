package com.co.bancolombia.franchise.domain.ports.in;

import com.co.bancolombia.franchise.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface GetFranchiseWithDetailsUseCase {
    Mono<Franchise> getFranchiseByName(String name);
}

