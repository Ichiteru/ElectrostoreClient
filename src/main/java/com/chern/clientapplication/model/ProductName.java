package com.chern.clientapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ProductName implements Serializable {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    public ProductName(String name) {
        this.name = name;
    }
}
