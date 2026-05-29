package com.co.bancolombia.franchise.domain.ports.in;

import com.co.bancolombia.franchise.domain.model.Product;
import reactor.core.publisher.Mono;

public interface AddProductUseCase {
    Mono<Product> addProduct(String franchiseName, String branchName, Product product);
}
