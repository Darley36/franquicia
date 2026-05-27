package com.co.bancolombia.franchise.domain.ports.in;

import com.co.bancolombia.franchise.domain.model.Product;
import reactor.core.publisher.Mono;

public interface UpdateProductNameUseCase {
    Mono<Product> updateName(String productId, String name);
}
