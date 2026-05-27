package com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.repository;

import com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.entity.FranchiseData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FranchiseDataRepository extends ReactiveMongoRepository<FranchiseData, String> {
    Mono<FranchiseData> findByName(String name);
}
