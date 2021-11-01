package com.chern.clientapplication.controller;

import com.chern.clientapplication.model.Product;
import com.chern.clientapplication.repository.ProductRepo;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FxmlView("user-page.fxml")
public class UserPageController extends Controller {

    @Autowired
    private ProductRepo productRepo;

    public TableView<Product> tableProducts;
    public TableColumn<Product, String> columnName;
    public TableColumn<Product, String> columnModel;
    public TableColumn<Product, String> columnSupplier;
    public TableColumn<Product, Double> columnUnitCost;
    public TableColumn<Product, Integer> columnAmount;
    @FXML
    private Pane paneSuppliers;
    @FXML
    private Pane paneStorage;
    @FXML
    private StackPane paneContainer;

    public void initialize(){
        productRepo.init();
        tableProducts.setItems(productRepo.getProductData());
        columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProductName().getName()));
        columnModel.setCellValueFactory(new PropertyValueFactory<Product, String>("model"));
        columnSupplier.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSupplier().getName()));
        columnUnitCost.setCellValueFactory(new PropertyValueFactory<Product, Double>("unitCost"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("amount"));
    }

    public void switchToStoragePane() {
        paneContainer.getChildren().clear();
        paneContainer.getChildren().add(paneStorage);
    }

    public void switchToSuppliersPane() {
        paneContainer.getChildren().clear();
        paneContainer.getChildren().add(paneSuppliers);
    }

    public void switchToSalesPane() {
    }

    public void switchToAdminPane() {
    }

    public void edit() {
    }

    public void delete() {
    }

    public void add() {
    }

    public void search() {
    }
}
