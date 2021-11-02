package com.chern.clientapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductName {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    public ProductName(String name) {
        this.name = name;
    }
}
