package com.co.bancolombia.franchise.domain.usecase;

import com.co.bancolombia.franchise.domain.enums.TechnicalMessage;
import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.ports.in.GetFranchiseWithDetailsUseCase;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class GetFranchiseWithDetailsUseCaseImpl implements GetFranchiseWithDetailsUseCase {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Franchise> getFranchiseByName(String name) {
        log.info("Getting franchise with details by name: {}", name);
        return validateAndNormalizeName(name)
                .flatMap(normalizedName -> franchiseRepository.findByName(normalizedName)
                        .switchIfEmpty(Mono.error(new BusinessException(
                                BusinessException.Type.ERROR_MONGO,
                                String.format(TechnicalMessage.FRANCHISE_NOT_FOUND_MSG.getMessage(), normalizedName)
                        )))
                );
    }

    private Mono<String> validateAndNormalizeName(String name) {
        return Mono.defer(() -> {
            String normalizedName = name != null ? name.trim() : "";
            if (normalizedName.isEmpty()) {
                return Mono.error(new IllegalArgumentException("Franchise name cannot be empty"));
            }
            return Mono.just(normalizedName);
        });
    }
}

