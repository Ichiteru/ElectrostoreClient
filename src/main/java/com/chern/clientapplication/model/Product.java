package com.chern.clientapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("unit_cost")
    private Double unitCost;
    @JsonProperty("model")
    private String model;
    @JsonProperty("supplier")
    private Supplier supplier;
    @JsonProperty("productName")
    private ProductName productName;

    public Product(Integer amount, Double unitCost, String model, Supplier supplier, ProductName productName) {
        this.amount = amount;
        this.unitCost = unitCost;
        this.model = model;
        this.supplier = supplier;
        this.productName = productName;
    }
}
