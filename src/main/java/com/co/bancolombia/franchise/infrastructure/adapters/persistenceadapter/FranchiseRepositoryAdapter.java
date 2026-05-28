package com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter;

import com.co.bancolombia.franchise.domain.exceptions.BusinessException;
import com.co.bancolombia.franchise.domain.model.Branch;
import com.co.bancolombia.franchise.domain.model.Franchise;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.entity.FranchiseData;
import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.mapper.FranchiseMapperPercistence;
import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.repository.FranchiseDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseDataRepository repository;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        log.info("Saving franchise: {}", franchise);
        return Mono.just(franchise)
                .map(FranchiseMapperPercistence::toData)
                .flatMap(repository::save)
                .map(FranchiseMapperPercistence::toDomain);
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        log.info("Finding franchise by name: {}", name);
        return repository.findByName(name)
                .map(FranchiseMapperPercistence::toDomain);
    }

    @Override
    public Mono<Branch> addBranchToFranchise(String franchiseId, Branch branch) {
        log.info("Adding branch: {} to franchise with id: {}", branch, franchiseId);

        Query query = Query.query(Criteria.where("id").is(franchiseId));
        Update update = new Update().push("branches", FranchiseMapperPercistence.toData(branch));

        return mongoTemplate.updateFirst(query, update, FranchiseData.class)
                .flatMap(result -> {
                    if (result.getModifiedCount() == 0) {
                        return Mono.error(new BusinessException(
                                BusinessException.Type.ERROR_MONGO,
                                "Franchise not found"));
                    }
                    return Mono.just(branch);
                });
    }

    @Override
    public Flux<Franchise> findAll() {
        log.info("Finding all franchises");
        return repository.findAll()
                .map(FranchiseMapperPercistence::toDomain);
    }
}
