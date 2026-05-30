package com.co.bancolombia.franchise.domain.usecase;

import com.co.bancolombia.franchise.domain.enums.TechnicalMessage;
import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.model.Product;
import com.co.bancolombia.franchise.domain.model.TopProductPerBranch;
import com.co.bancolombia.franchise.domain.ports.in.FindMaxProductStockUseCase;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FindMaxProductStockUseCaseImpl implements FindMaxProductStockUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Flux<TopProductPerBranch> findTopProductPerBranch(String franchiseName) {
        log.info("Finding top product per branch for franchise: {}", franchiseName);

        return validateAndNormalizeName(franchiseName)
                .flatMapMany(normalizedName -> findFranchiseByName(normalizedName)
                        .flatMapMany(franchise -> buildTopProductsPerBranch(franchise)));
    }

    private Mono<String> validateAndNormalizeName(String franchiseName) {
        return Mono.defer(() -> {
            String normalized = franchiseName != null ? franchiseName.trim() : "";
            if (normalized.isEmpty()) {
                return Mono.error(new IllegalArgumentException("Franchise name cannot be empty"));
            }
            return Mono.just(normalized);
        });
    }

    private Mono<Franchise> findFranchiseByName(String franchiseName) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new BusinessException(
                        BusinessException.Type.ERROR_MONGO,
                        String.format(TechnicalMessage.FRANCHISE_NOT_FOUND_MSG.getMessage(), franchiseName))));
    }

    private Flux<TopProductPerBranch> buildTopProductsPerBranch(Franchise franchise) {
        List<Branch> branches = getBranchesOrEmpty(franchise);

        if (branches.isEmpty()) {
            return Flux.empty();
        }

        return Flux.fromIterable(branches)
                .flatMap(branch -> findTopProduct(branch));
    }

    private Mono<TopProductPerBranch> findTopProduct(Branch branch) {
        return Mono.defer(() -> {
            List<Product> products = getProductsOrEmpty(branch);

            if (products.isEmpty()) {
                return Mono.empty();
            }

            return products.stream()
                    .max(Comparator.comparingInt(p -> p.getStock() != null ? p.getStock() : 0))
                    .map(topProduct -> Mono.just(TopProductPerBranch.builder()
                            .branchName(branch.getName())
                            .topProduct(topProduct)
                            .build()))
                    .orElse(Mono.empty());
        });
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
