package com.chern.clientapplication.utils;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    public void showAlert(AlertType alertType) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(alertType.title);
        alert.setHeaderText(alertType.header);
        alert.setContentText(alertType.message);
        alert.show();
    }


    public enum AlertType {
        PASSWORD_REGEX_WARNING(
                "Некорректно задан пароль",
                "Обратите внимание на требования",
                "• Не менее 8 символов\n" +
                "• Содержит как минимум одну цифру\n" +
                "• Содержит по крайней мере один символ нижнего и один символ верхнего алфавита\n" +
                "• Содержит хотя бы один символ из набора специальных символов (@#%$^ и т.д.)\n" +
                "• Не содержит пробела, табуляции и т.д."),
        PASSWORD_OR_LOGIN_IS_EMPTY(
                "Данные не заполнены",
                "Обратите внимание на требование",
                "Поле ввода логина или пароля не должно быть пустым."),
        USER_NOT_FOUND(
                "Ошибка при входе",
                "Повторите попытку",
                "Неверный логин или пароль."),
        BRAND_IS_EMPTY(
                "Данные не заполнены",
                "Обратите внимание на требование",
                "Поле ввода марки не должно быть пустым."
        ),
        PASSWORDS_NOT_MATCHES(
                "Пароли не совпадают",
                        "Повторите попытку",
                        "Пароли не совпадают."
        ),
        USER_ALREADY_EXISTS(
                "Ошибка регистрации",
                "Повторите попытку",
                "Пользователь с указанным именем уже существует."
        ),
        SOME_FIELD_IS_EMPTY(
                "Ошибка при отправке",
                        "Повторите попытку",
                        "Заполните все необходимые поля."
        ),
        WRONG_VALUE(
                "Ошибка при отправке",
                "Повторите попытку",
                "Введите корректное значение."
        ),
        MODEL_ALREADY_EXISTS(
                "Ошибка при добавлении",
                "Повторите попытку",
                "Продукт с таким названием модели уже существует."
        );

        private final String title;
        private final String message;
        private final String header;

        AlertType(String title, String header, String message) {
            this.title = title;
            this.header = header;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getTitle() {
            return title;
        }
    }
}
