package com.co.bancolombia.franchise.infrastructure.adapters.persistenceadapter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "franchiseData")
public class FranchiseData {
    @Id
    private String id;
    private String name;
}
