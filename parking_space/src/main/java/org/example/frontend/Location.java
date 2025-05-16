package org.example.frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;

public class Location extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox vbox = createVBox();
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setTitle("Парковка");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createVBox() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Button registration = createRegistrationButton();
        Button departure = createDepartureButton();
        Button history = createHistoryButton();
        Button search = createSearchButton();
        Button state = createStateButton();
        Button statistics = createStatisticsButton();

        root.getChildren().addAll(registration, departure, history, search, state, statistics);

        return root;
    }

    private Button createRegistrationButton() {
        Button button = new Button("Регистрация");
        customizeButton(button);
        button.setOnAction(event -> handleRegistrationAction()); // Используем лямбда-выражение
        return button;
    }

    private Button createDepartureButton() {
        Button button = new Button("Выезд");
        customizeButton(button);
        button.setOnAction(event -> handleDepartureAction());
        return button;
    }

    private Button createHistoryButton() {
        Button button = new Button("История");
        customizeButton(button);
        button.setOnAction(event -> handleHistoryAction());
        return button;
    }

    private Button createSearchButton() {
        Button button = new Button("Поиск");
        customizeButton(button);
        button.setOnAction(event -> handleSearchAction());
        return button;
    }

    private Button createStateButton() {
        Button button = new Button("Текущее состояние");
        customizeButton(button);
        button.setOnAction(event -> handleStateAction());
        return button;
    }

    private Button createStatisticsButton() {
        Button button = new Button("Статистика");
        customizeButton(button);
        button.setOnAction(event -> handleStatisticsAction());
        return button;
    }

    private void customizeButton(Button button) {
        double buttonHeight = 40;
        button.setPrefHeight(buttonHeight);
        button.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(button, Priority.ALWAYS);
    }

    // Методы обработки действий кнопок
    private void handleRegistrationAction() {
        System.out.println("Кнопка 'Регистрация' нажата!");
        // Добавьте здесь код для обработки нажатия кнопки "Регистрация"
    }

    private void handleDepartureAction() {
        System.out.println("Кнопка 'Выезд' нажата!");
        // Добавьте здесь код для обработки нажатия кнопки "Выезд"
    }

    private void handleHistoryAction() {
        System.out.println("Кнопка 'История' нажата!");
        // Добавьте здесь код для обработки нажатия кнопки "История"
    }

    private void handleSearchAction() {
        System.out.println("Кнопка 'Поиск' нажата!");
        // Добавьте здесь код для обработки нажатия кнопки "Поиск"
    }

    private void handleStateAction() {
        System.out.println("Кнопка 'Текущее состояние' нажата!");
        // Добавьте здесь код для обработки нажатия кнопки "Текущее состояние"
    }

    private void handleStatisticsAction() {
        System.out.println("Кнопка 'Статистика' нажата!");
        // Добавьте здесь код для обработки нажатия кнопки "Статистика"
    }

    public static void main(String[] args) {
        launch(args);
    }
}
