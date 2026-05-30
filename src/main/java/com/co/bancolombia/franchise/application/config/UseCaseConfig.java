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

    @Bean
    public AddProductUseCase addProductUseCase(FranchiseRepository franchiseRepository) {
        return new AddProductUseCaseImpl(franchiseRepository);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(FranchiseRepository franchiseRepository) {
        return new DeleteProductUseCaseImpl(franchiseRepository);
    }

    @Bean
    public UpdateProductStockUseCase updateProductStockUseCase(FranchiseRepository franchiseRepository) {
        return new UpdateProductStockUseCaseImpl(franchiseRepository);
    }

    @Bean
    public FindMaxProductStockUseCase findMaxProductStockUseCase(FranchiseRepository franchiseRepository) {
        return new FindMaxProductStockUseCaseImpl(franchiseRepository);
    }

    @Bean
    public GetFranchiseWithDetailsUseCase getFranchiseWithDetailsUseCase(FranchiseRepository franchiseRepository) {
        return new GetFranchiseWithDetailsUseCaseImpl(franchiseRepository);
    }

}
