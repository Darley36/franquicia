package com.co.bancolombia.franchise.domain.ports.in;

import com.co.bancolombia.franchise.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface UpdateFranchiseNameUseCase {
    Mono<Franchise> updateName(String franchiseId, String name);
}
