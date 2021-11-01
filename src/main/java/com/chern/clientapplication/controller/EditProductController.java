package com.chern.clientapplication.controller;

import com.chern.clientapplication.model.Product;
import com.chern.clientapplication.model.ProductName;
import com.chern.clientapplication.model.Supplier;
import com.chern.clientapplication.repository.ProductNameRepo;
import com.chern.clientapplication.repository.ProductRepo;
import com.chern.clientapplication.repository.SupplierRepo;
import com.chern.clientapplication.utils.AlertService;
import com.chern.clientapplication.utils.EmptyFieldValidationService;
import com.chern.clientapplication.utils.NumberValidationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@FxmlView("edit-product-modal.fxml")
@RequiredArgsConstructor
public class EditProductController extends Controller{
    public static final String TITLE = "Редактирование товара";
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private ProductNameRepo productNameRepo;
    @Autowired
    ProductRepo productRepo;
    private final EmptyFieldValidationService validationService;
    private final AlertService alertService;
    private final NumberValidationService numberValidationService;
    @FXML
    private TextField textFieldModel;
    @FXML
    private ComboBox<ProductName> cbProductName;
    @FXML
    private TextField textFieldUnitCost;
    @FXML
    private TextField textFieldAmount;
    @FXML
    private ComboBox<Supplier> cbSupplier;

    public void initialize(){
        supplierRepo.init();
        productNameRepo.init();
        cbProductName.setItems(productNameRepo.getProductNameDataProd());
        cbSupplier.setItems(supplierRepo.getSupplierDataProd());
        cbSupplier.setConverter(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier supplier) {
                if (supplier != null)
                    return supplier.getName();
                return "";
            }

            @Override
            public Supplier fromString(String s) {
                return null;
            }
        });
        cbProductName.setConverter(new StringConverter<ProductName>() {
            @Override
            public String toString(ProductName productName) {
                if (productName != null)
                    return productName.getName();
                return "";
            }

            @Override
            public ProductName fromString(String s) {
                return null;
            }
        });
        fillFieldsById(productRepo.getSelectedItemId());
    }


    private Product getNewProduct(){
        return new Product(Integer.parseInt(textFieldAmount.getText()),
                Double.parseDouble(textFieldUnitCost.getText()),
                textFieldModel.getText(),
                cbSupplier.getValue(),
                cbProductName.getValue());
    }

    private void fillFieldsById(Long id){
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(productRepo.getSelectedItemId()));
        Product product = restClient.getForObject(SERVER_URL + "/product/{id}", Product.class, params);
        System.out.println(product);
        textFieldAmount.setText(String.valueOf(product.getAmount()));
        textFieldUnitCost.setText(String.valueOf(product.getUnitCost()));
        textFieldModel.setText(product.getModel());
        cbProductName.setValue(product.getProductName());
        cbSupplier.setValue(product.getSupplier());
    }

    public void edit() {
        if (validationService.isEmpty(textFieldModel)
                || validationService.isEmpty(textFieldAmount)
                || validationService.isEmpty(textFieldUnitCost)
                || validationService.isEmpty(cbProductName)
                || validationService.isEmpty(cbSupplier)) {
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        }
        else if (!numberValidationService.isValidInteger(textFieldAmount.getText())
                || !numberValidationService.isValidDouble(textFieldUnitCost.getText())){
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        } else {
            Product product = getNewProduct();
            product.setId(productRepo.getSelectedItemId());
//            restClient.put(SERVER_URL + "/product/update/{id}", getNewProduct(), params);
            ResponseEntity<Boolean> result = restClient.exchange(SERVER_URL + "/product/update", HttpMethod.PUT, new HttpEntity<>(product), Boolean.class);
            if (!result.getBody().booleanValue()){
                alertService.showAlert(AlertService.AlertType.MODEL_ALREADY_EXISTS);
            }
        }
    }
}
