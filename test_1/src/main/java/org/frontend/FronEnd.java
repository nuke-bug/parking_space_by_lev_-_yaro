package org.frontend;

// Для JavaFX
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;

import org.bd.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.bd.bd.ParkingHistory;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;

import static org.bd.bd.*;


public class FronEnd extends Application {
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

    public static void customizeButton(Button button) {
        double buttonHeight = 40;
        button.setPrefHeight(buttonHeight);
        button.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(button, Priority.ALWAYS);
    }

    // Методы обработки действий кнопок
    public static void handleRegistrationAction() {

        try {
            Stage stage = new Stage();
            TableView<ParkingSpace> table = new TableView<>();

            // Создание колонок
            TableColumn<ParkingSpace, Integer> numberCol = new TableColumn<>("Номер места");
            numberCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumber()).asObject());

            TableColumn<ParkingSpace, String> typeCol = new TableColumn<>("Тип места");
            typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

            TableColumn<ParkingSpace, String> costCol = new TableColumn<>("Стоимость аренды в час");
            costCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCost()));

            numberCol.setPrefWidth(150);       // Ширина для номера
            typeCol.setPrefWidth(150);       // Ширина для владельца
            costCol.setPrefWidth(150);  // Ширина для оплаты
            // Добавляем все колонки в таблицу
            table.getColumns().addAll(
                    numberCol,
                    typeCol,
                    costCol
            );
            // Загрузка данных
            table.getItems().setAll(get_free_places());
            // Настройка окна
            stage.setScene(new Scene(table, 1000, 600));
            stage.setTitle("Свободные места");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка: " + e.getMessage()).show();
        }

        Stage registrationStage = new Stage();  // Создаем новое окно
        registrationStage.setTitle("Регистрация автомобиля");

        // Создаем поля ввода
        TextField fioField = new TextField();
        fioField.setPromptText("ФИО");
        TextField carModelField = new TextField();
        carModelField.setPromptText("Марка машины");
        TextField licensePlateField = new TextField();
        licensePlateField.setPromptText("Номер машины");
        TextField freeSpaceField = new TextField();
        freeSpaceField.setPromptText("Номер свободного места");

        // Получаем текущее время
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        final String registrationTime = formattedDateTime;
        Label currentTimeLabel = new Label("Текущее время: " + formattedDateTime);

        // Создаем компоновку для полей ввода
        VBox registrationLayout = new VBox(10);
        registrationLayout.setPadding(new Insets(10));
        registrationLayout.getChildren().addAll(
                new Label("ФИО:"), fioField,
                new Label("Марка машины:"), carModelField,
                new Label("Номер машины:"), licensePlateField,
                new Label("Свободное место: "), freeSpaceField,
                currentTimeLabel
        );

        Button saveButton = new Button("Сохранить");
        customizeButton(saveButton);
        registrationLayout.getChildren().add(saveButton); // Добавляем кнопку "Сохранить"

        Scene registrationScene = new Scene(registrationLayout, 300, 400);  // Создаем сцену
        registrationStage.setScene(registrationScene);  // Устанавливаем сцену для окна
        registrationStage.show();  // Отображаем окно
        System.out.println("Кнопка 'Регистрация' нажата!");
        // Создаем кнопку "Сохранить" (если нужно сохранять данные)
        saveButton.setOnAction(e -> {
            try{
                String fio = fioField.getText();
                String carModel = carModelField.getText();
                String licensePlate = licensePlateField.getText();
                String freeSpace = freeSpaceField.getText();

                if (fio.isEmpty() || carModel.isEmpty() || licensePlate.isEmpty() || freeSpace.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Пожалуйста, заполните все поля.").show();
                return; // Прерываем выполнение, если поля не заполнены
            }
                List<String> registration = new ArrayList<>();
                registration.add(fio);
                registration.add(carModel);
                registration.add(licensePlate);
                registration.add(registrationTime);
                registration.add(freeSpace);

                if (!check_new_client(registration)){
                    new Alert(Alert.AlertType.WARNING, "Пожалуйста, используйте свободные места.").show();
                    return;
                }
                post_new_client(registration);
                // Закрываем окно после сохранения
                registrationStage.close();
            } catch (Exception e_1) {
                e_1.printStackTrace();
            }
            System.out.println("Кнопка 'Сохранить' нажата!");
        });
        // Добавьте здесь код для обработки нажатия кнопки "Регистрация"
    }

    public static void handleDepartureAction() {
        // departureTime
        Stage departureStage = new Stage();
        departureStage.setTitle("Выезд автомобиля");

        // Создаем поле ввода для номера машины
        TextField licensePlateField = new TextField();
        licensePlateField.setPromptText("Номер машины");

        // Получаем текущее время
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        final String departureTime = formattedDateTime;
        Label currentTimeLabel = new Label("Текущее время: " + formattedDateTime);

        // Создаем компоновку
        VBox departureLayout = new VBox(10);
        departureLayout.setPadding(new Insets(10));
        departureLayout.getChildren().addAll(
                new Label("Номер машины:"), licensePlateField,
                currentTimeLabel
        );
        System.out.println("Время выезда: " + departureTime);

        // Создаем кнопку "Подтвердить выезд"
        Button confirmDepartureButton = new Button("Подтвердить выезд");
        confirmDepartureButton.setOnAction(e -> {
            List<String> departure = new ArrayList<>();
            String licensePlate = licensePlateField.getText();
            departure.add(departureTime);
            departure.add(licensePlateField.getText());
            System.out.println(departure);

            try{
                if (!check_old_client(departure.get(1))){
                new Alert(Alert.AlertType.WARNING, "Пожалуйста, оплатите существующую машину.").show();
                departure = new ArrayList<>();
                return;
            }
                String totalPrise = get_cost(bd.post_old_client(departure), departureTime);
                post_cost(totalPrise, departure);
                Stage priceStage = new Stage();
                priceStage.setTitle("Стоимость парковки");

                Label priceLabel = new Label("К оплате: " + totalPrise + " рублей");
                VBox priceLayout = new VBox(10, priceLabel);
                priceLayout.setAlignment(Pos.CENTER);
                priceLayout.setPadding(new Insets(20));

                Scene priceScene = new Scene(priceLayout, 250, 100);
                priceStage.setScene(priceScene);
                priceStage.show();
            } catch (Exception e_1) {
                e_1.printStackTrace();
            }

            departureStage.close(); // Закрываем окно выезда
        });

        departureLayout.getChildren().add(confirmDepartureButton);

        Scene departureScene = new Scene(departureLayout, 300, 300);
        departureStage.setScene(departureScene);
        departureStage.show();
        System.out.println("Кнопка 'Выезд' нажата!");
        // Добавьте здесь код для обработки нажатия кнопки "Выезд"
    }

    public static void handleCarHistoryAction(String car_number) {
        try {
            Stage stage = new Stage();
            TableView<ParkingHistory> table = new TableView<>();

            // Создание колонок
            TableColumn<ParkingHistory, Integer> numberCol = new TableColumn<>("№");
            numberCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumber()).asObject());

            TableColumn<ParkingHistory, String> ownerCol = new TableColumn<>("Владелец");
            ownerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner()));

            TableColumn<ParkingHistory, String> carNumberCol = new TableColumn<>("Номер авто");
            carNumberCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarNumber()));

            TableColumn<ParkingHistory, String> carBrandCol = new TableColumn<>("Марка авто");
            carBrandCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarBrand()));

            TableColumn<ParkingHistory, String> checkInCol = new TableColumn<>("Время заезда");
            checkInCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCheckInTime()));

            TableColumn<ParkingHistory, String> departureCol = new TableColumn<>("Время выезда");
            departureCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartureTime()));

            TableColumn<ParkingHistory, String> paymentCol = new TableColumn<>("Оплата");
            paymentCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPayment()));
            numberCol.setPrefWidth(50);       // Ширина для номера
            ownerCol.setPrefWidth(150);       // Ширина для владельца
            carNumberCol.setPrefWidth(100);   // Ширина для номера авто
            carBrandCol.setPrefWidth(150);    // Ширина для марки авто
            checkInCol.setPrefWidth(150);     // Ширина для времени заезда
            departureCol.setPrefWidth(150);   // Ширина для времени выезда
            paymentCol.setPrefWidth(100);     // Ширина для оплаты
            // Добавляем все колонки в таблицу
            table.getColumns().addAll(
                    numberCol,
                    ownerCol,
                    carNumberCol,
                    carBrandCol,
                    checkInCol,
                    departureCol,
                    paymentCol
            );
            // Загрузка данных
            table.getItems().setAll(find_in_history(car_number));
            // Настройка окна
            stage.setScene(new Scene(table, 1000, 600));
            stage.setTitle("История для данной машины парковки");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка: " + e.getMessage()).show();
        }
    }

    private TableView<ParkingHistory> tableView;

    private void handleHistoryAction() {
        try {
            Stage stage = new Stage();
            Label totalPaymentLabel = null;
            TableView<ParkingHistory> table = new TableView<>();

            // Создание колонок
            TableColumn<ParkingHistory, Integer> numberCol = new TableColumn<>("№");
            numberCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumber()).asObject());

            TableColumn<ParkingHistory, String> ownerCol = new TableColumn<>("Владелец");
            ownerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner()));

            TableColumn<ParkingHistory, String> carNumberCol = new TableColumn<>("Номер авто");
            carNumberCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarNumber()));

            TableColumn<ParkingHistory, String> carBrandCol = new TableColumn<>("Марка авто");
            carBrandCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarBrand()));

            TableColumn<ParkingHistory, String> checkInCol = new TableColumn<>("Время заезда");
            checkInCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCheckInTime()));

            TableColumn<ParkingHistory, String> departureCol = new TableColumn<>("Время выезда");
            departureCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartureTime()));

            TableColumn<ParkingHistory, String> paymentCol = new TableColumn<>("Оплата");
            paymentCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPayment()));
            numberCol.setPrefWidth(50);       // Ширина для номера
            ownerCol.setPrefWidth(150);       // Ширина для владельца
            carNumberCol.setPrefWidth(100);   // Ширина для номера авто
            carBrandCol.setPrefWidth(150);    // Ширина для марки авто
            checkInCol.setPrefWidth(150);     // Ширина для времени заезда
            departureCol.setPrefWidth(150);   // Ширина для времени выезда
            paymentCol.setPrefWidth(100);     // Ширина для оплаты
            // Добавляем все колонки в таблицу
            table.getColumns().addAll(
                    numberCol,
                    ownerCol,
                    carNumberCol,
                    carBrandCol,
                    checkInCol,
                    departureCol,
                    paymentCol
            );
            // Загрузка данных
            table.getItems().setAll(getHistory());
            /*
            Button calculateButton = new Button("Рассчитать общий доход");

            calculateButton.setOnAction(e -> {
                String total = totalPayment(tableView.getItems()); // Вызываем метод напрямую
                totalPaymentLabel.setText("Общий доход: " + total);
            });

            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(10));
            vbox.getChildren().addAll(tableView, calculateButton, totalPaymentLabel);
            */
            // Настройка окна
            stage.setScene(new Scene(table, 1000, 600));
            stage.setTitle("История парковки");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка: " + e.getMessage()).show();
        }
    }

    public static String handleSearchAction() {
        // licensePlate
        Stage searchStage = new Stage();
        searchStage.setTitle("Поиск по номеру");

        VBox searchLayout = new VBox(10);
        searchLayout.setPadding(new Insets(10));

        TextField searchField = new TextField();
        searchField.setPromptText("Введите номер машины");

        Button searchConfirmButton = new Button("Искать");
        customizeButton(searchConfirmButton);

        Label resultLabel = new Label(""); // Для отображения результатов поиска

        searchConfirmButton.setOnAction(event -> {
            String licensePlate = searchField.getText();  // Получаем номер
            handleCarHistoryAction(licensePlate);

            System.out.println("Номер машины: " + licensePlate);
//            CarInfo carInfo = findCarByLicensePlate(licensePlate);
//
//            if (carInfo != null) {
//                resultLabel.setText("ФИО: " + carInfo.ownerName + ", Модель: " + carInfo.carModel);
//            } else {
//                resultLabel.setText("Машина с таким номером не найдена.");
//            }
        });

        searchLayout.getChildren().addAll(searchField, searchConfirmButton, resultLabel);

        Scene searchScene = new Scene(searchLayout, 300, 200);
        searchStage.setScene(searchScene);
        searchStage.show();
        System.out.println("Кнопка 'Поиск' нажата!");
        return searchField.getText().toUpperCase();
        // Добавьте здесь код для обработки нажатия кнопки "Поиск"
    }

    private void handleStateAction() {//не сделано!
        System.out.println("Кнопка 'Текущее состояние' нажата!");
        try {
            Stage stage = new Stage();
            TableView<ParkingSpace> table = new TableView<>();

            // Создание колонок
            TableColumn<ParkingSpace, Integer> numberCol = new TableColumn<>("Номер места");
            numberCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumber()).asObject());

            TableColumn<ParkingSpace, String> typeCol = new TableColumn<>("Тип места");
            typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

            TableColumn<ParkingSpace, String> busynessCol = new TableColumn<>("Занятость");
            busynessCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarBusyness()));

            TableColumn<ParkingSpace, String> costCol = new TableColumn<>("Стоимость аренды в час");
            costCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCost()));

            numberCol.setPrefWidth(150);       // Ширина для номера
            typeCol.setPrefWidth(150);       // Ширина для владельца
            busynessCol.setPrefWidth(150);   // Ширина для номера авто
            costCol.setPrefWidth(150);  // Ширина для оплаты
            // Добавляем все колонки в таблицу
            table.getColumns().addAll(
                    numberCol,
                    typeCol,
                    busynessCol,
                    costCol
            );
            // Загрузка данных
            table.getItems().setAll(get_parking_spaces());
            // Настройка окна
            stage.setScene(new Scene(table, 1000, 600));
            stage.setTitle("Текущее состояние");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка: " + e.getMessage()).show();
        }
    }

    private void handleStatisticsAction() {
        System.out.println("Кнопка 'Статистика' нажата!");
        try {
            Stage stage = new Stage();
            TableView<ParkingHistory> table = new TableView<>();

            // Создание колонок
            TableColumn<ParkingHistory, Integer> numberCol = new TableColumn<>("№");
            numberCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumber()).asObject());

            TableColumn<ParkingHistory, String> ownerCol = new TableColumn<>("Владелец");
            ownerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner()));

            TableColumn<ParkingHistory, String> carNumberCol = new TableColumn<>("Номер авто");
            carNumberCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarNumber()));

            TableColumn<ParkingHistory, String> carBrandCol = new TableColumn<>("Марка авто");
            carBrandCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarBrand()));

            TableColumn<ParkingHistory, String> checkInCol = new TableColumn<>("Время заезда");
            checkInCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCheckInTime()));

            TableColumn<ParkingHistory, String> departureCol = new TableColumn<>("Время выезда");
            departureCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartureTime()));

            TableColumn<ParkingHistory, String> paymentCol = new TableColumn<>("Оплата");
            paymentCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPayment()));
            numberCol.setPrefWidth(50);       // Ширина для номера
            ownerCol.setPrefWidth(150);       // Ширина для владельца
            carNumberCol.setPrefWidth(100);   // Ширина для номера авто
            carBrandCol.setPrefWidth(150);    // Ширина для марки авто
            checkInCol.setPrefWidth(150);     // Ширина для времени заезда
            departureCol.setPrefWidth(150);   // Ширина для времени выезда
            paymentCol.setPrefWidth(100);     // Ширина для оплаты
            // Добавляем все колонки в таблицу
            table.getColumns().addAll(
                    numberCol,
                    ownerCol,
                    carNumberCol,
                    carBrandCol,
                    checkInCol,
                    departureCol,
                    paymentCol
            );
            // Загрузка данных
            table.getItems().setAll(get_history_this_day());
            // Настройка окна
            stage.setScene(new Scene(table, 1000, 600));
            stage.setTitle("История парковки за день");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка: " + e.getMessage()).show();
        }
        // Добавьте здесь код для обработки нажатия кнопки "Статистика"
    }
    public static String get_cost(List<String> pay, String depature_time) {
        String cost = pay.get(0);
        String check_in_time = pay.get(1);
        int pricePerHour = Integer.parseInt(cost);
        LocalDateTime departureTime = null;
        LocalDateTime checkInTime = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            departureTime = LocalDateTime.parse(depature_time, formatter);
            checkInTime = LocalDateTime.parse(check_in_time, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Ошибка при разборе даты: " + e.getMessage());
        }

        Duration duration = Duration.between(checkInTime, departureTime);

        long hours = duration.toHours(); // Общее количество *полных* часов

        double totalCost = (hours + 1) * pricePerHour;
        System.out.println(totalCost);
        // Добавить функцию которая запихнет её в бд
        return String.valueOf(totalCost);
    }

    public static String totalPayment (ObservableList<ParkingHistory> data) {
        BigDecimal totalPay = BigDecimal.ZERO;
        for (ParkingHistory history : data) {
            String paymentString = history.getPayment();
            BigDecimal payment = new BigDecimal(paymentString);
            totalPay = totalPay.add(payment);
        }
        return totalPay.toString();
    }


    public static void main(String[] args) {
        try{
            bd_main();
        } catch (Exception e) {
            e.printStackTrace();
        }

        launch(args);
    }
}
