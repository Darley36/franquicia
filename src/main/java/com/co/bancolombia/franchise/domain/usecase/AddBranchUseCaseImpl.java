package com.co.bancolombia.franchise.domain.usecase;

import com.co.bancolombia.franchise.domain.enums.TechnicalMessage;
import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.ports.in.AddBranchUseCase;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class AddBranchUseCaseImpl implements AddBranchUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Branch> addBranch(String franchiseName, Branch branch) {
        log.info("Adding branch: {} to franchise: {}", branch, franchiseName);

        return validateInputsAndNormalize(franchiseName, branch)
                .flatMap(tuple -> findFranchise(tuple.getT1())
                        .flatMap(franchise -> validateBranchNotExists(franchise, branch.toBuilder().name(tuple.getT2()).build())))
                .flatMap(franchise -> addBranchToFranchise(franchise.getId(), branch));
    }

    private Mono<Franchise> findFranchise(String franchiseName) {
        return franchiseRepository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new BusinessException(
                        BusinessException.Type.ERROR_MONGO,
                        String.format(TechnicalMessage.FRANCHISE_NOT_FOUND_MSG.getMessage(), franchiseName))));
    }

    private Mono<Tuple2<String,String>> validateInputsAndNormalize(String franchiseName, Branch branch) {
        return Mono.defer(() -> {
            if (branch == null) {
                return Mono.error(new IllegalArgumentException("Branch cannot be null"));
            }
            String normalizedFranchiseName = franchiseName != null ? franchiseName.trim() : "";
            String normalizedBranchName = branch.getName() != null ? branch.getName().trim() : "";
            if (normalizedFranchiseName.isEmpty()) {
                return Mono.error(new IllegalArgumentException("Franchise name cannot be empty"));
            }
            if (normalizedBranchName.isEmpty()) {
                return Mono.error(new IllegalArgumentException("Branch name cannot be empty"));
            }
            return Mono.just(Tuples.of(normalizedFranchiseName, normalizedBranchName));
        });
    }

    private Mono<Franchise> validateBranchNotExists(Franchise franchise, Branch branch) {
        List<Branch> branches = getBranchesOrEmpty(franchise);

        if (isBranchExists(branch, branches)) {
            return Mono.error(new BusinessException(
                    BusinessException.Type.ERROR_MONGO,
                    String.format(TechnicalMessage.BRANCH_EXISTS_MSG.getMessage(), branch.getName())));
        }
        return Mono.just(franchise);
    }

    private static boolean isBranchExists(Branch branch, List<Branch> branches) {
        return branches.stream()
                .anyMatch(b -> b.getName().equalsIgnoreCase(branch.getName().trim()));
    }

    private Mono<Branch> addBranchToFranchise(String franchiseId, Branch branch) {

        Branch newBranch = branch.toBuilder()
                .id(UUID.randomUUID().toString())
                .name(branch.getName().trim())
                .products(new ArrayList<>())
                .build();

        return franchiseRepository.addBranchToFranchise(franchiseId, newBranch)
                .thenReturn(newBranch);
    }

    private List<Branch> getBranchesOrEmpty(Franchise franchise) {
        return franchise.getBranches() != null ?
                new ArrayList<>(franchise.getBranches()) :
                new ArrayList<>();
    }
}
