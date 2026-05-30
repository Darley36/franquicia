package com.co.bancolombia.franchise.domain.usecase;

import com.co.bancolombia.franchise.domain.enums.TechnicalMessage;
import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.model.Product;
import com.co.bancolombia.franchise.domain.ports.in.DeleteProductUseCase;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Void> deleteProduct(String franchiseName, String branchName, String productName) {
        log.info("Deleting product: '{}' from branch: '{}' in franchise: '{}'", productName, branchName, franchiseName);

        return validateInputsAndNormalize(franchiseName, branchName, productName)
                .flatMap(tuple -> findFranchiseByName(tuple.getT1())
                        .flatMap(franchise -> findBranchByName(franchise, tuple.getT2())
                                .flatMap(branch -> findProductByName(branch, tuple.getT3())
                                        .flatMap(product -> franchiseRepository.deleteProductFromBranch(
                                                franchise.getId(), branch.getId(), product.getId())))));
    }

    private Mono<Tuple3<String, String, String>> validateInputsAndNormalize(String franchiseName, String branchName, String productName) {
        return Mono.defer(() -> {
            String normalizedFranchiseName = franchiseName != null ? franchiseName.trim() : "";
            String normalizedBranchName    = branchName != null    ? branchName.trim()    : "";
            String normalizedProductName   = productName != null   ? productName.trim()   : "";

            if (normalizedFranchiseName.isEmpty()) {
                return Mono.error(new IllegalArgumentException("Franchise name cannot be empty"));
            }
            if (normalizedBranchName.isEmpty()) {
                return Mono.error(new IllegalArgumentException("Branch name cannot be empty"));
            }
            if (normalizedProductName.isEmpty()) {
                return Mono.error(new IllegalArgumentException("Product name cannot be empty"));
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
