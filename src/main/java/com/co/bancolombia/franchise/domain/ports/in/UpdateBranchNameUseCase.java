package com.co.bancolombia.franchise.domain.ports.in;

import com.co.bancolombia.franchise.domain.model.Branch;
import reactor.core.publisher.Mono;

public interface UpdateBranchNameUseCase {
    Mono<Branch> updateName(String branchId, String name);
}
