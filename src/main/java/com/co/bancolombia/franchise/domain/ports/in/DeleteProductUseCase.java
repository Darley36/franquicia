package com.co.bancolombia.franchise.domain.ports.in;

import reactor.core.publisher.Mono;

public interface DeleteProductUseCase {
    Mono<Void> deleteProduct(String franchiseName, String branchName, String productName);
}
