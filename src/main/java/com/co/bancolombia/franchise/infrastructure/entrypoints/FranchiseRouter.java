package com.co.bancolombia.franchise.infrastructure.entrypoints;

import com.co.bancolombia.franchise.infrastructure.entrypoints.handler.FranchiseHandler;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class FranchiseRouter {

    private final FranchiseHandler handler;

    public FranchiseRouter(FranchiseHandler handler) {
        this.handler = handler;
    }

    @Bean
    @RouterOperations({
        @RouterOperation(path = "/api/franchises",            method = RequestMethod.POST,
                beanClass = FranchiseHandler.class, beanMethod = "createFranchise",
                operation = @Operation(operationId = "createFranchise",   summary = "Create a new franchise",   tags = {"Franchise"})),
        @RouterOperation(path = "/api/franchises/{franchiseId}/branches", method = RequestMethod.POST,
                beanClass = FranchiseHandler.class, beanMethod = "addBranch",
                operation = @Operation(operationId = "addBranch",          summary = "Add a branch to franchise", tags = {"Branch"})),
        @RouterOperation(path = "/api/branches/{branchId}/products",      method = RequestMethod.POST,
                beanClass = FranchiseHandler.class, beanMethod = "addProduct",
                operation = @Operation(operationId = "addProduct",         summary = "Add a product to branch",   tags = {"Product"})),
        @RouterOperation(path = "/api/branches/{branchId}/products/{productId}", method = RequestMethod.DELETE,
                beanClass = FranchiseHandler.class, beanMethod = "deleteProduct",
                operation = @Operation(operationId = "deleteProduct",      summary = "Delete a product from branch", tags = {"Product"})),
        @RouterOperation(path = "/api/branches/{branchId}/products/{productId}/stock", method = RequestMethod.PATCH,
                beanClass = FranchiseHandler.class, beanMethod = "updateProductStock",
                operation = @Operation(operationId = "updateProductStock", summary = "Update product stock",      tags = {"Product"})),
        @RouterOperation(path = "/api/franchises/{franchiseId}/top-products", method = RequestMethod.GET,
                beanClass = FranchiseHandler.class, beanMethod = "getTopProducts",
                operation = @Operation(operationId = "getTopProducts",     summary = "Top product per branch",    tags = {"Franchise"}))
    })
    public RouterFunction<ServerResponse> franchiseRoutes() {
        return RouterFunctions.route()
                .POST("/api/franchises",
                        handler::createFranchise)
                .POST("/api/franchises/{franchiseName}/branches",
                        handler::addBranch)
                .POST("/api/franchises/{franchiseName}/branches/{branchName}/products",
                        handler::addProduct)
                //.GET("/api/franchises/{franchiseId}/top-products",
                //        handler::getTopProducts)
                .build();
    }
}
