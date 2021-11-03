package com.chern.clientapplication.controller;

import com.chern.clientapplication.model.Product;
import com.chern.clientapplication.model.Sale;
import com.chern.clientapplication.model.User;
import com.chern.clientapplication.repository.SaleRepo;
import com.chern.clientapplication.utils.AlertService;
import com.chern.clientapplication.utils.NumberValidationService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@FxmlView("sale-product-modal.fxml")
@RequiredArgsConstructor
public class SaleProductController extends Controller{

    public static final String TITLE = "Продажа";

    @FXML
    private TextField tfAmountForSale;
    private final AlertService alertService;
    private final NumberValidationService numberValidationService;


    public void signDeal() {
         Product productForSale = SaleRepo.getSelectedProduct();
        User user = getMyUser();
        if (numberValidationService.isValidInteger(tfAmountForSale.getText())){
            if (Integer.parseInt(tfAmountForSale.getText()) > productForSale.getAmount()){
                alertService.showAlert(AlertService.AlertType.SALE_PRODUCT_COUNT_MORE_THAN_PRODUCTS_ON_WAREHOUSE);
            } else {
                Integer amount = Integer.valueOf(tfAmountForSale.getText());
                Sale sale = new Sale(productForSale.getProductName().getName(),
                        productForSale.getModel(),
                        amount, amount * productForSale.getUnitCost(),
                        getMyUser().getUsername());
                restClient.postForObject(SERVER_URL + "/sale", new HttpEntity<>(sale), Sale.class);
                productForSale.setAmount(productForSale.getAmount() - amount);
                restClient.exchange(SERVER_URL + "/product/update", HttpMethod.PUT, new HttpEntity<>(productForSale), Boolean.class);
            }
        } else alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
    }

}
