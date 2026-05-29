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
    //private final FindMaxProductStockUseCase findMaxProductStockUseCase;
    //private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    //private final UpdateBranchNameUseCase updateBranchNameUseCase;
    //private final UpdateProductNameUseCase updateProductNameUseCase;
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

    // POST /api/branches/{branchId}/products
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


}
