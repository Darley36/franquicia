package com.co.bancolombia.franchise.domain.usecase;

import com.co.bancolombia.franchise.domain.enums.TechnicalMessage;
import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.ports.in.CreateFranchiseUseCase;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class CreateFranchiseUseCaseImpl implements CreateFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        log.info("Creating franchise: {}", franchise);
        return validateAndNormalizeName(franchise)
                .flatMap(normalizedName -> franchiseRepository.findByName(normalizedName)
                        .flatMap(existingFranchise -> {
                            log.warn("Franchise with name {} already exists", normalizedName);
                            return Mono.<Franchise>error(new BusinessException
                                    (BusinessException.Type.ERROR_MONGO,
                                            String.format(TechnicalMessage.FRANCHISE_EXISTS_MSG.getMessage(), normalizedName)));
                        })
                        .switchIfEmpty(franchiseRepository.saveFranchise(franchise.toBuilder().name(normalizedName).build()))
                );
    }

    private Mono<String> validateAndNormalizeName(Franchise franchise) {
        return Mono.defer(() -> {
            if (franchise == null) {
                return Mono.error(new IllegalArgumentException("Franchise cannot be null"));
            }
            String normalizedName = franchise.getName() != null ? franchise.getName().trim() : "";
            if (normalizedName.isEmpty()) {
                return Mono.error(new IllegalArgumentException("Franchise name cannot be empty"));
            }
            return Mono.just(normalizedName);
        });
    }
}
