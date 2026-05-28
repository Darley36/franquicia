package com.co.bancolombia.franchise.application.config;

import com.co.bancolombia.franchise.domain.ports.in.*;
import com.co.bancolombia.franchise.domain.ports.out.FranchiseRepository;
import com.co.bancolombia.franchise.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateFranchiseUseCase createFranchiseUseCase(FranchiseRepository franchiseRepository) {
        return new CreateFranchiseUseCaseImpl(franchiseRepository);
    }

    @Bean
    public AddBranchUseCase addBranchUseCase(FranchiseRepository franchiseRepository) {
        return new AddBranchUseCaseImpl(franchiseRepository);
    }

}
