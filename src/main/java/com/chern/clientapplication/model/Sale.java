package com.chern.clientapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Sale {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_model")
    private String productModel;
    @JsonProperty("sale_amount")
    private Integer saleAmount;
    @JsonProperty("total_cost")
    private Double totalCost;
    @JsonProperty("dealer_name")
    private String dealerName;

    public Sale(String productName, String productModel, Integer saleAmount, Double totalCost, String dealerName) {
        this.date = LocalDate.now();
        this.productName = productName;
        this.productModel = productModel;
        this.saleAmount = saleAmount;
        this.totalCost = totalCost;
        this.dealerName = dealerName;
    }
}
