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
        return Mono.just(franchise.getName())
                .map(String::trim)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise name cannot be empty")))
                .flatMap(name -> franchiseRepository.findByName(name)
                        .flatMap(existingFranchise -> Mono.<Franchise>error(new BusinessException
                                (BusinessException.Type.ERROR_MONGO,
                                        String.format(TechnicalMessage.FRANCHISE_EXISTS_MSG.getMessage(), name))))
                        .switchIfEmpty(franchiseRepository.saveFranchise(franchise))
                );
    }
}
