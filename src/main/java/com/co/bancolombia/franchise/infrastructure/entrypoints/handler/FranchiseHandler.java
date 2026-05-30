package com.co.bancolombia.franchise.infrastructure.entrypoints.handler;

import com.co.bancolombia.franchise.domain.ports.in.*;
import com.co.bancolombia.franchise.infrastructure.entrypoints.dto.*;
import com.co.bancolombia.franchise.infrastructure.entrypoints.mapper.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final AddBranchUseCase addBranchUseCase;
    private final AddProductUseCase addProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final FindMaxProductStockUseCase findMaxProductStockUseCase;
    private final GetFranchiseWithDetailsUseCase getFranchiseWithDetailsUseCase;
    private final GlobalErrorHandler globalErrorHandler;

    // POST /api/franchises
    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseRequestDto.class)
                .map(FranchiseMapper::toDomain)
                .flatMap(createFranchiseUseCase::createFranchise)
                .map(FranchiseMapper::toResponseDto)
                .flatMap(franchiseDto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(franchiseDto))
                .onErrorResume(e -> globalErrorHandler.handlerError(e, request));
    }

    // POST /api/franchises/{franchiseName}/branches
    public Mono<ServerResponse> addBranch(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        return request.bodyToMono(BranchRequestDto.class)
                .map(FranchiseMapper::toDomain)
                .flatMap(branch -> addBranchUseCase.addBranch(franchiseName, branch))
                .map(FranchiseMapper::toResponseDto)
                .flatMap(branchDto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(branchDto))
                .onErrorResume(e -> globalErrorHandler.handlerError(e, request));
    }

    // POST /api/franchises/{franchiseName}/branches/{branchName}/products
    public Mono<ServerResponse> addProduct(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        String branchName = request.pathVariable("branchName");
        return request.bodyToMono(ProductRequestDto.class)
                .map(FranchiseMapper::toDomain)
                .flatMap(product -> addProductUseCase.addProduct(franchiseName, branchName, product))
                .map(FranchiseMapper::toResponseDto)
                .flatMap(productDto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(productDto))
                .onErrorResume(e -> globalErrorHandler.handlerError(e, request));
    }

    // DELETE /api/franchises/{franchiseName}/branches/{branchName}/products/{productName}
    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        String branchName    = request.pathVariable("branchName");
        String productName   = request.pathVariable("productName");
        return deleteProductUseCase.deleteProduct(franchiseName, branchName, productName)
                .then(ServerResponse.noContent().build())
                .onErrorResume(e -> globalErrorHandler.handlerError(e, request));
    }

    // PATCH /api/franchises/{franchiseName}/branches/{branchName}/products/{productName}/stock
    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        String branchName    = request.pathVariable("branchName");
        String productName   = request.pathVariable("productName");
        return request.bodyToMono(StockUpdateDto.class)
                .flatMap(stockDto -> updateProductStockUseCase.updateStock(franchiseName, branchName, productName, stockDto.getStock()))
                .map(FranchiseMapper::toResponseDto)
                .flatMap(productDto -> ServerResponse.ok().bodyValue(productDto))
                .onErrorResume(e -> globalErrorHandler.handlerError(e, request));
    }

    // GET /api/franchises/{franchiseName}/top-products
    public Mono<ServerResponse> getTopProducts(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        return findMaxProductStockUseCase.findTopProductPerBranch(franchiseName)
                .map(FranchiseMapper::toResponseDto)
                .collectList()
                .flatMap(list -> ServerResponse.ok().bodyValue(list))
                .onErrorResume(e -> globalErrorHandler.handlerError(e, request));
    }

    // GET /api/franchises/{franchiseName}
    public Mono<ServerResponse> getFranchiseWithDetails(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        return getFranchiseWithDetailsUseCase.getFranchiseByName(franchiseName)
                .map(FranchiseMapper::toResponseDto)
                .flatMap(franchiseDto -> ServerResponse.ok().bodyValue(franchiseDto))
                .onErrorResume(e -> globalErrorHandler.handlerError(e, request));
    }

}
