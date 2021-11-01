package com.chern.clientapplication.controller;

import com.chern.clientapplication.model.Product;
import com.chern.clientapplication.model.ProductName;
import com.chern.clientapplication.model.Supplier;
import com.chern.clientapplication.model.UserStatus;
import com.chern.clientapplication.repository.ProductNameRepo;
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
import org.springframework.stereotype.Component;

@Component
@FxmlView("add-product-modal.fxml")
@RequiredArgsConstructor
public class AddProductController extends Controller{

    public static final String TITLE = "Новый товар";

    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private ProductNameRepo productNameRepo;
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
    }

    public void add() {
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
            HttpEntity<Product> productHttpEntity = new HttpEntity<>(getNewProduct());
            Boolean result = restClient.postForObject(SERVER_URL + "/product", productHttpEntity, Boolean.class);
            if (!result){
                alertService.showAlert(AlertService.AlertType.MODEL_ALREADY_EXISTS);
            }
        }
    }

    private Product getNewProduct(){
        return new Product(Integer.parseInt(textFieldAmount.getText()),
                Double.parseDouble(textFieldUnitCost.getText()),
                textFieldModel.getText(),
                cbSupplier.getValue(),
                cbProductName.getValue());
    }


}
