package com.co.bancolombia.franchise.domain.ports.in;

import com.co.bancolombia.franchise.domain.model.TopProductPerBranch;
import reactor.core.publisher.Flux;

public interface FindMaxProductStockUseCase {
    Flux<TopProductPerBranch> findTopProductPerBranch(String franchiseName);
}
