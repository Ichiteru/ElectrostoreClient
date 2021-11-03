package com.chern.clientapplication.controller;

import com.chern.clientapplication.model.Product;
import com.chern.clientapplication.model.ProductName;
import com.chern.clientapplication.model.Sale;
import com.chern.clientapplication.model.Supplier;
import com.chern.clientapplication.repository.ProductNameRepo;
import com.chern.clientapplication.repository.ProductRepo;
import com.chern.clientapplication.repository.SaleRepo;
import com.chern.clientapplication.repository.SupplierRepo;
import com.chern.clientapplication.utils.AlertService;
import com.chern.clientapplication.utils.EmptyFieldValidationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
@FxmlView("user-page.fxml")
public class UserPageController extends Controller {

    @FXML private TextField tfSalesSearch;
    @FXML private Label lbIncome;
    @FXML private TableView<Sale> tableSales;
    @FXML private TableColumn<Sale, String> colProductName;
    @FXML private TableColumn<Sale, String> colModel;
    @FXML private TableColumn<Sale, Integer> colTotalAmount;
    @FXML private TableColumn<Sale, Double> colTotalCost;
    @FXML private TableColumn<Sale, String> colDate;
    @FXML private TableColumn<Sale, String> colDealerName;
    @FXML
    private TextField textFieldSearch;
    @FXML
    private TableView<Supplier> tableSupplier;
    @FXML
    private TableColumn<Supplier, Long> colSupplierId;
    @FXML
    private TableColumn<Supplier, String> colSupplierName;
    @FXML
    private TextField tfSupplierName;
    @FXML
    private TableView<ProductName> tableProductName;
    @FXML
    private TableColumn<ProductName, Long> colProductNameId;
    @FXML
    private TableColumn<ProductName, String> colProductNameName;
    @FXML
    private TextField tfProductName;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductNameRepo productNameRepo;
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private SaleRepo saleRepo;
    private final EmptyFieldValidationService validationService;
    private final AlertService alertService;

    @FXML private TableView<Product> tableProducts;
    @FXML private TableColumn<Product, String> columnName;
    @FXML private TableColumn<Product, String> columnModel;
    @FXML private TableColumn<Product, String> columnSupplier;
    @FXML private TableColumn<Product, Double> columnUnitCost;
    @FXML private TableColumn<Product, Integer> columnAmount;
    @FXML
    private Pane paneSales;
    @FXML
    private Pane paneSuppliers;
    @FXML
    private Pane paneStorage;
    @FXML
    private StackPane paneContainer;

    public void initialize(){
        productRepo.init();
        productNameRepo.init();
        supplierRepo.init();
        saleRepo.init();
        FilteredList<Product> filteredProductData = getProductFilteredList(productRepo.getProductData(), textFieldSearch);
        tableProducts.setItems(filteredProductData);
        FilteredList<Sale> filteredSaleData = getSaleFilteredList(saleRepo.getSaleDataProd(), tfSalesSearch);
        tableSales.setItems(filteredSaleData);
        columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProductName().getName()));
        columnModel.setCellValueFactory(new PropertyValueFactory<Product, String>("model"));
        columnSupplier.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSupplier().getName()));
        columnUnitCost.setCellValueFactory(new PropertyValueFactory<Product, Double>("unitCost"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("amount"));

        tableSupplier.setItems(supplierRepo.getSupplierDataProd());
        colSupplierId.setCellValueFactory(new PropertyValueFactory<Supplier, Long>("id"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));

        tableProductName.setItems(productNameRepo.getProductNameDataProd());
        colProductNameId.setCellValueFactory(new PropertyValueFactory<ProductName, Long>("id"));
        colProductNameName.setCellValueFactory(new PropertyValueFactory<ProductName, String>("name"));

        colProductName.setCellValueFactory(new PropertyValueFactory<Sale, String>("productName"));
        colModel.setCellValueFactory(new PropertyValueFactory<Sale, String>("productModel"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<Sale, Integer>("saleAmount"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<Sale, Double>("totalCost"));
        colDealerName.setCellValueFactory(new PropertyValueFactory<Sale, String>("dealerName"));
        colDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate().toString()));
        lbIncome.setText(String.valueOf(getTotalIncome()) + " $");

    }

    private FilteredList<Product> getProductFilteredList(ObservableList<Product> products, TextField textField) {
        FilteredList<Product> filteredData = getProductFilteredData(products, textField);
        return filteredData;
    }

    private FilteredList<Product> getProductFilteredData(ObservableList<Product> products, TextField textField) {
        FilteredList<Product> filteredData = new FilteredList<>(products, p -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (product.getProductName().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (product.getModel().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (product.getAmount().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (product.getSupplier().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (product.getUnitCost().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        return filteredData;
    }

    private FilteredList<Sale> getSaleFilteredList(ObservableList<Sale> sales, TextField textField) {
        FilteredList<Sale> filteredData = getSaleFilteredData(sales, textField);
        return filteredData;
    }

    private FilteredList<Sale> getSaleFilteredData(ObservableList<Sale> sales, TextField textField) {
        FilteredList<Sale> filteredData = new FilteredList<>(sales, p -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sale -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (sale.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (sale.getProductModel().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (sale.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (sale.getDealerName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (sale.getTotalCost().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (sale.getSaleAmount().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        return filteredData;
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
        paneContainer.getChildren().clear();
        paneContainer.getChildren().add(paneSales);
    }

    public void switchToAdminPane() {

    }

    public void edit() {
        if (tableProducts.getSelectionModel().getSelectedItem() != null){
            productRepo.setSelectedItemId(tableProducts.getSelectionModel().getSelectedItem().getId());
            showNewStageWindow(EditProductController.class);
        } else alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
    }

    public void delete() {
        Product selectedItem = tableProducts.getSelectionModel().getSelectedItem();
        if (selectedItem == null){
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(selectedItem.getId()));
            restClient.delete(SERVER_URL + "/product/{id}", params);
            productRepo.init();
            tableProducts.setItems(productRepo.getProductData());
        }
    }

    public void add() {
        showNewStageWindow(AddProductController.class);
    }

    public void update() {
        productRepo.init();
        tableProducts.setItems(getProductFilteredList(productRepo.getProductData(), textFieldSearch));
    }

    public void addSupplier() {
        String supplierName = tfSupplierName.getText();
        if (validationService.isEmpty(tfSupplierName)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else {
            HttpEntity<Supplier> supplierHttpEntity = new HttpEntity<>(new Supplier(supplierName));
            Boolean result = restClient.postForObject(SERVER_URL + "/supplier", supplierHttpEntity, Boolean.class);
            if(!result){
                alertService.showAlert(AlertService.AlertType.SUPPLIER_ALREADY_EXISTS);
            } else {
                supplierRepo.init();
                tableSupplier.setItems(supplierRepo.getSupplierDataProd());
            }
        }
    }

    public void deleteSupplier() {
        Supplier selectedItem = tableSupplier.getSelectionModel().getSelectedItem();
        if (selectedItem == null){
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(selectedItem.getId()));
            try {
                restClient.delete(SERVER_URL + "/supplier/{id}", params);
                supplierRepo.init();
                tableSupplier.setItems(supplierRepo.getSupplierDataProd());
            } catch (HttpServerErrorException ex){
                alertService.showAlert(AlertService.AlertType.SUPPLIER_HAS_SUPPLIES);
            }
        }
    }

    public void addProductName() {
        String productNameName = tfProductName.getText();
        if (validationService.isEmpty(tfProductName)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else {
            HttpEntity<ProductName> prNameHttpEntity = new HttpEntity<>(new ProductName(productNameName));
            Boolean result = restClient.postForObject(SERVER_URL + "/productName", prNameHttpEntity, Boolean.class);
            if(!result){
                alertService.showAlert(AlertService.AlertType.PRODUCT_NAME_ALREADY_EXISTS);
            } else {
                productNameRepo.init();
                tableProductName.setItems(productNameRepo.getProductNameDataProd());
            }
        }
    }

    public void deleteProductName() {
        ProductName selectedItem = tableProductName.getSelectionModel().getSelectedItem();
        if (selectedItem == null){
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(selectedItem.getId()));
            try {
                restClient.delete(SERVER_URL + "/productName/{id}", params);
                productNameRepo.init();
                tableProductName.setItems(productNameRepo.getProductNameDataProd());
            } catch (HttpServerErrorException ex){
                alertService.showAlert(AlertService.AlertType.PRODUCT_WITH_THIS_NAME_THERE_IS_IN_WAREHOUSE);
            }
        }
    }

    public void salesSearch() {
    }

    public Double getTotalIncome(){
        AtomicReference<Double> income = new AtomicReference<>(0.0);
        tableSales.getItems().forEach(sale -> {
            income.updateAndGet(v -> v + sale.getTotalCost());
        });
        return income.get();
    }

    public void sale() {
        if (tableProducts.getSelectionModel().getSelectedItem() != null){
            SaleRepo.setSelectedProduct(tableProducts.getSelectionModel().getSelectedItem());
            showNewStageWindow(SaleProductController.class);
        } else alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
    }

    public void updateSales(ActionEvent actionEvent) {
        saleRepo.init();
        tableSales.setItems(getSaleFilteredList(saleRepo.getSaleDataProd(), textFieldSearch));
        lbIncome.setText(String.valueOf(getTotalIncome()) + " $");
    }
}
