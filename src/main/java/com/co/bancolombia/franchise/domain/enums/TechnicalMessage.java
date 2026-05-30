package com.co.bancolombia.franchise.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    FRANCHISE_EXISTS_MSG("Franchise with name '%s' already exists"),
    FRANCHISE_NOT_FOUND_MSG("Franchise with name '%s' not found"),
    BRANCH_EXISTS_MSG("Branch with name '%s' already exists in this franchise"),
    BRANCH_NOT_FOUND_MSG("Branch with name '%s' not found in franchise '%s'"),
    PRODUCT_NOT_FOUND_MSG("Product with name '%s' not found in branch '%s'");

    private final String message;
}