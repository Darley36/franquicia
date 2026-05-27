package com.co.bancolombia.franchise.domain.ports.in;

import com.co.bancolombia.franchise.domain.model.Product;
import reactor.core.publisher.Flux;

public interface FindMaxProductStockUseCase {
    Flux<Product> findTopProductPerBranch(String franchiseId);
}
