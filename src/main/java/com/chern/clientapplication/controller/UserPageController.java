package com.chern.clientapplication.controller;

import com.chern.clientapplication.model.*;
import com.chern.clientapplication.repository.*;
import com.chern.clientapplication.utils.AlertService;
import com.chern.clientapplication.utils.EmptyFieldValidationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colUserName;
    @FXML private TableColumn<User, String> colUserPassword;
    @FXML private TableColumn<User, String> colUserRole;
    @FXML private TextField tfUserName;
    @FXML private ComboBox<Role> cbUserRole;
    @FXML private TextField tfUserPassword;
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
    private UserRepo userRepo;
    @Autowired
    private SaleRepo saleRepo;
    private final EmptyFieldValidationService validationService;
    private final AlertService alertService;
    private User selectedUser;

    @FXML private TableView<Product> tableProducts;
    @FXML private TableColumn<Product, String> columnName;
    @FXML private TableColumn<Product, String> columnModel;
    @FXML private TableColumn<Product, String> columnSupplier;
    @FXML private TableColumn<Product, Double> columnUnitCost;
    @FXML private TableColumn<Product, Integer> columnAmount;
    @FXML
    private Pane paneAdmin;
    @FXML
    private Pane paneSales;
    @FXML
    private Pane paneSuppliers;
    @FXML
    private Pane paneStorage;
    @FXML
    private StackPane paneContainer;

    public void initialize(){
        selectedUser = new User();
        paneContainer.getChildren().clear();
        paneContainer.getChildren().add(paneStorage);
        initRepo();
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

        cbUserRole.setItems(FXCollections.observableArrayList(Role.USER, Role.ADMIN));
        tableUsers.setItems(userRepo.getUserDataProd());
        colUserName.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        colUserPassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        colUserRole.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getRole().toString()));
        tableUsers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observableValue, User user, User newValue) {
                if (newValue == null)
                    newValue = selectedUser;
                selectedUser = newValue;
                System.out.println(newValue);
                tfUserName.setText(newValue.getUsername());
                tfUserPassword.setText(newValue.getPassword());
                cbUserRole.setValue(newValue.getRole());
            }
        });

        colProductName.setCellValueFactory(new PropertyValueFactory<Sale, String>("productName"));
        colModel.setCellValueFactory(new PropertyValueFactory<Sale, String>("productModel"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<Sale, Integer>("saleAmount"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<Sale, Double>("totalCost"));
        colDealerName.setCellValueFactory(new PropertyValueFactory<Sale, String>("dealerName"));
        colDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate().toString()));
        lbIncome.setText(String.valueOf(getTotalIncome()) + " $");

    }

    private void initRepo() {
            productRepo.init();
            productNameRepo.init();
            supplierRepo.init();
            saleRepo.init();
            userRepo.init();
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
        if (getMyUser().getRole().equals(Role.ADMIN)){
            paneContainer.getChildren().clear();
            paneContainer.getChildren().add(paneAdmin);
        }
        else {
            alertService.showAlert(AlertService.AlertType.NO_ACCESS_USER);
        }
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

    public void updateSales() {
        saleRepo.init();
        tableSales.setItems(getSaleFilteredList(saleRepo.getSaleDataProd(), textFieldSearch));
        lbIncome.setText(String.valueOf(getTotalIncome()) + " $");
    }

    public void addOrEditUser() {
        if (validationService.isEmpty(tfUserName) || validationService.isEmpty(tfUserPassword)
        || validationService.isEmpty(cbUserRole)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else {
            User user = new User(tfUserName.getText(), tfUserPassword.getText(), cbUserRole.getValue());
            if (selectedUser.getUsername().equals(user.getUsername())){
                user.setId(selectedUser.getId());
                restClient.put(SERVER_URL + "/user", user);
            } else {
                restClient.postForObject(SERVER_URL + "/user", user, Boolean.class);
            }
        }
    }

    public void deleteUser() {
        if (tableUsers.getSelectionModel().getSelectedItem() != null){
            User user = tableUsers.getSelectionModel().getSelectedItem();
            Map<String,String> params = new HashMap<>();
            params.put("id", String.valueOf(user.getId()));
            restClient.delete(SERVER_URL + "/user/{id}", params);
            tableUsers.getItems().remove(user);
            tableUsers.refresh();
        } else {
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        }
    }

    public void updateUsers() {
        userRepo.init();
        tableUsers.setItems(userRepo.getUserDataProd());
    }
}
