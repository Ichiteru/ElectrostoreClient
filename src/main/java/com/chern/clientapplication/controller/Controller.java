package com.chern.clientapplication.controller;

import com.chern.clientapplication.model.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public abstract  class Controller {

    protected final static String SERVER_URL = "http://localhost:8083/";
    protected final static String TITLE = "Новое окно";

    @Autowired
    protected FxWeaver fxWeaver;
    @Autowired
    protected RestTemplate restClient;

    public static Stage primaryStage;

    public void showCurrentStageWindow(Class<? extends Controller> controllerClass, String title) {
        showStage(controllerClass, title, primaryStage);
    }

    public void showNewStageWindow(Class<? extends Controller> controllerClass) {
        try {
            showStage(controllerClass, getControllerTitle(controllerClass), new Stage());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void showStage(Class<? extends Controller> controllerClass, String title, Stage stage) {
        Parent root = fxWeaver.loadView(controllerClass);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    private String getControllerTitle(Class<? extends Controller> controllerClass)
            throws IllegalAccessException, NoSuchFieldException {
        return controllerClass.getDeclaredField("TITLE").get(this).toString();
    }

    protected HttpHeaders getJsonHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected void initRepo(){
//        carRepo.init();
//        userRepo.init();
    }

    protected User getMyUser(){
        User user = restClient.getForObject(SERVER_URL + "my", User.class);
        return user;
    }

}
