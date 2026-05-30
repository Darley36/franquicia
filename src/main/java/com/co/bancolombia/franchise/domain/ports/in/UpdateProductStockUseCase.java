package com.co.bancolombia.franchise.domain.ports.in;

import com.co.bancolombia.franchise.domain.model.Product;
import reactor.core.publisher.Mono;

public interface UpdateProductStockUseCase {
    Mono<Product> updateStock(String franchiseName, String branchName, String productName, int stock);
}
