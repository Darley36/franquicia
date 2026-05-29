package com.co.bancolombia.franchise.domain.usecase;

import com.co.bancolombia.franchise.domain.enums.TechnicalMessage;
import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.domain.exceptions.ValidationException;
import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.model.Product;
import com.co.bancolombia.franchise.domain.ports.in.AddProductUseCase;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class AddProductUseCaseImpl implements AddProductUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Product> addProduct(String franchiseName, String branchName, Product product) {
        log.info("Adding product: {} to branch: {} in franchise: {}", product, branchName, franchiseName);

        return validateInputsAndNormalize(franchiseName, branchName, product)
                .flatMap(tuple -> findFranchiseByName(tuple.getT1())
                        .flatMap(franchise -> findBranchInFranchise(franchise, tuple.getT2())
                                .flatMap(branch -> validateProductNotExists(branch, product.toBuilder().name(tuple.getT3()).build()))
                                .map(branch -> Tuples.of(franchise.getId(), branch.getId()))))
                .flatMap(tuple -> addProductToBranch(tuple.getT1(), tuple.getT2(), product));
    }

    private Mono<Tuple3<String, String, String>> validateInputsAndNormalize(String franchiseName, String branchName, Product product) {
        return Mono.defer(() -> {
            if (product == null) {
                return Mono.error(new ValidationException("Product cannot be null", ValidationException.Type.MISSING_FIELD));
            }
            String normalizedFranchiseName = franchiseName != null ? franchiseName.trim() : "";
            String normalizedBranchName = branchName != null ? branchName.trim() : "";
            String normalizedProductName = product.getName() != null ? product.getName().trim() : "";

            if (normalizedFranchiseName.isEmpty()) {
                return Mono.error(new ValidationException("Franchise name cannot be empty", ValidationException.Type.MISSING_FIELD));
            }
            if (normalizedBranchName.isEmpty()) {
                return Mono.error(new ValidationException("Branch name cannot be empty", ValidationException.Type.MISSING_FIELD));
            }
            if (normalizedProductName.isEmpty()) {
                return Mono.error(new ValidationException("Product name cannot be empty", ValidationException.Type.MISSING_FIELD));
            }
            if (product.getStock() == null || product.getStock() <= 0) {
                return Mono.error(new ValidationException("Stock must be greater than 0", ValidationException.Type.INVALID_INPUT));
            }
            return Mono.just(Tuples.of(normalizedFranchiseName, normalizedBranchName, normalizedProductName));
        });
    }

    private Mono<Franchise> findFranchiseByName(String franchiseName) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new BusinessException(
                        BusinessException.Type.ERROR_MONGO,
                        String.format(TechnicalMessage.FRANCHISE_NOT_FOUND_MSG.getMessage(), franchiseName))));
    }

    private Mono<Branch> findBranchInFranchise(Franchise franchise, String branchName) {
        return Mono.defer(() -> {
            List<Branch> branches = getBranchesOrEmpty(franchise);
            return branches.stream()
                    .filter(branch -> branch.getName().equals(branchName))
                    .findFirst()
                    .map(Mono::just)
                    .orElseGet(() -> Mono.error(new BusinessException(
                            BusinessException.Type.ERROR_MONGO,
                            String.format("Branch with name '%s' not found in franchise", branchName))));
        });
    }

    private Mono<Branch> validateProductNotExists(Branch branch, Product product) {
        return Mono.defer(() -> {
            List<Product> products = getProductsOrEmpty(branch);

            if (isProductExists(product.getName(), products)) {
                return Mono.error(new BusinessException(
                        BusinessException.Type.ERROR_MONGO,
                        String.format("Product with name '%s' already exists in this branch", product.getName())));
            }
            return Mono.just(branch);
        });
    }

    private boolean isProductExists(String productName, List<Product> products) {
        return products.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(productName));
    }

    private Mono<Product> addProductToBranch(String franchiseId, String branchId, Product product) {
        Product newProduct = product.toBuilder()
                .id(UUID.randomUUID().toString())
                .name(product.getName().trim())
                .build();

        return franchiseRepository.addProductToBranch(franchiseId, branchId, newProduct)
                .thenReturn(newProduct);
    }

    private List<Branch> getBranchesOrEmpty(Franchise franchise) {
        return franchise.getBranches() != null ?
                new ArrayList<>(franchise.getBranches()) :
                new ArrayList<>();
    }

    private List<Product> getProductsOrEmpty(Branch branch) {
        return branch.getProducts() != null ?
                new ArrayList<>(branch.getProducts()) :
                new ArrayList<>();
    }
}
