package com.co.bancolombia.franchise.domain.usecase;

import com.co.bancolombia.franchise.domain.enums.TechnicalMessage;
import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.domain.exceptions.ValidationException;
import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.model.Product;
import com.co.bancolombia.franchise.domain.ports.in.UpdateProductStockUseCase;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UpdateProductStockUseCaseImpl implements UpdateProductStockUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Product> updateStock(String franchiseName, String branchName, String productName, int stock) {
        log.info("Updating stock of product: '{}' in branch: '{}' in franchise: '{}' to: {}", productName, branchName, franchiseName, stock);

        return validateInputsAndNormalize(franchiseName, branchName, productName, stock)
                .flatMap(tuple -> findFranchiseByName(tuple.getT1())
                        .flatMap(franchise -> findBranchByName(franchise, tuple.getT2())
                                .flatMap(branch -> findProductByName(branch, tuple.getT3())
                                        .flatMap(product -> franchiseRepository.updateProductStock(
                                                franchise.getId(), branch.getId(), product.getId(), stock)))));
    }

    private Mono<Tuple3<String, String, String>> validateInputsAndNormalize(String franchiseName, String branchName, String productName, int stock) {
        return Mono.defer(() -> {
            String normalizedFranchiseName = franchiseName != null ? franchiseName.trim() : "";
            String normalizedBranchName    = branchName != null    ? branchName.trim()    : "";
            String normalizedProductName   = productName != null   ? productName.trim()   : "";

            if (normalizedFranchiseName.isEmpty()) {
                return Mono.error(new ValidationException("Franchise name cannot be empty", ValidationException.Type.MISSING_FIELD));
            }
            if (normalizedBranchName.isEmpty()) {
                return Mono.error(new ValidationException("Branch name cannot be empty", ValidationException.Type.MISSING_FIELD));
            }
            if (normalizedProductName.isEmpty()) {
                return Mono.error(new ValidationException("Product name cannot be empty", ValidationException.Type.MISSING_FIELD));
            }
            if (stock < 0) {
                return Mono.error(new ValidationException("Stock cannot be negative", ValidationException.Type.INVALID_INPUT));
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

    private Mono<Branch> findBranchByName(Franchise franchise, String branchName) {
        return Mono.defer(() -> {
            List<Branch> branches = getBranchesOrEmpty(franchise);
            return branches.stream()
                    .filter(b -> b.getName().equalsIgnoreCase(branchName))
                    .findFirst()
                    .map(Mono::just)
                    .orElseGet(() -> Mono.error(new BusinessException(
                            BusinessException.Type.ERROR_MONGO,
                            String.format(TechnicalMessage.BRANCH_NOT_FOUND_MSG.getMessage(), branchName, franchise.getName()))));
        });
    }

    private Mono<Product> findProductByName(Branch branch, String productName) {
        return Mono.defer(() -> {
            List<Product> products = getProductsOrEmpty(branch);
            return products.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(productName))
                    .findFirst()
                    .map(Mono::just)
                    .orElseGet(() -> Mono.error(new BusinessException(
                            BusinessException.Type.ERROR_MONGO,
                            String.format(TechnicalMessage.PRODUCT_NOT_FOUND_MSG.getMessage(), productName, branch.getName()))));
        });
    }

    private List<Branch> getBranchesOrEmpty(Franchise franchise) {
        return franchise.getBranches() != null ? franchise.getBranches() : List.of();
    }

    private List<Product> getProductsOrEmpty(Branch branch) {
        return branch.getProducts() != null ? branch.getProducts() : List.of();
    }
}
