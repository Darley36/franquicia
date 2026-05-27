package com.co.bancolombia.franchise.domain.ports.in;

import reactor.core.publisher.Mono;

public interface DeleteProductUseCase {
    Mono<Void> deleteProduct(String branchId, String productId);
}
