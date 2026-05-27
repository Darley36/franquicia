package com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter;

import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.mapper.FranchiseMapperPercistence;
import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.repository.FranchiseDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseDataRepository repository;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return Mono.error(new UnsupportedOperationException("Saving franchise is not supported yet"));
        //return Mono.just(franchise)
        //        .map(FranchiseMapperPercistence::toData)
        //        .flatMap(repository::save)
        //        .map(FranchiseMapperPercistence::toDomain);
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        log.info("Finding franchise by name: {}", name);
        return repository.findByName(name)
                .map(FranchiseMapperPercistence::toDomain);
    }
}
