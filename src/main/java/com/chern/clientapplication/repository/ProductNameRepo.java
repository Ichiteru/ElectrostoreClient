package com.chern.clientapplication.repository;

import com.chern.clientapplication.controller.Controller;
import com.chern.clientapplication.model.ProductName;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductNameRepo extends Controller {

    private ObservableList<ProductName> productNameDataProd = FXCollections.observableArrayList();
    private List<ProductName> productNameListProd = new ArrayList<>();
    private ResponseEntity<List<ProductName>> productNameData;

    public ObservableList<ProductName> getProductNameDataProd() {
        return productNameDataProd;
    }

    public List<ProductName> getProductNameListProd() {
        return productNameListProd;
    }

    public void init(){
        productNameDataProd.clear();
        productNameData = restClient.exchange(SERVER_URL + "productNames",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductName>>(){});
        productNameData.getBody().forEach(p -> {
            productNameDataProd.add(p);
            productNameListProd.add(p);
        });
    }
}
