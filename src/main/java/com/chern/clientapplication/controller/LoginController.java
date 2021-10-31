package com.chern.clientapplication.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("login.fxml")
public class LoginController extends Controller{

//    private final FxControllerAndView<SomeDialog, VBox> someDialog;
//
//    @FXML
//    public Button openDialogButton;
//
//
//    public LoginController(FxControllerAndView<SomeDialog, VBox> someDialog) {
//        this.someDialog = someDialog;
//    }
//
//    @FXML
//    public void initialize() {
//        openDialogButton.setOnAction(
//                actionEvent -> someDialog.getController().show()
//        );
//    }

}
