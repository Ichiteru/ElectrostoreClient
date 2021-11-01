package com.chern.clientapplication.utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Service;

@Service
public class EmptyFieldValidationService {

    public boolean isEmpty(TextField textField){
        if (textField.getText() == "")
            return true;
        return false;
    }

    public boolean isEmpty(ComboBox<?> comboBox){
        if (comboBox.getSelectionModel().getSelectedItem() == null)
            return true;
        return false;
    }
}
