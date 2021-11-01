package com.chern.clientapplication.repository;

import com.chern.clientapplication.controller.Controller;
import com.chern.clientapplication.model.Product;
import com.chern.clientapplication.model.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SupplierRepo extends Controller {

    private ObservableList<Supplier> supplierDataProd = FXCollections.observableArrayList();
    private List<Supplier> supplierListProd = new ArrayList<>();
    private ResponseEntity<List<Supplier>> supplierData;

    public ObservableList<Supplier> getSupplierDataProd() {
        return supplierDataProd;
    }

    public void init(){
        supplierDataProd.clear();
        supplierData = restClient.exchange(SERVER_URL + "suppliers",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Supplier>>(){});
        supplierData.getBody().forEach(p -> {
            supplierDataProd.add(p);
            supplierListProd.add(p);
        });
    }
}
