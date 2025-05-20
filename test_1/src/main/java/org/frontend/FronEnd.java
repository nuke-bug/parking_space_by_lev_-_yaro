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

        List<String> registration = new ArrayList<>();
        // fio, carModel, licensePlate, registrationTime, freeSpaceField
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
        try{
            bd.get_free_places();
        } catch (Exception e_1) {
            e_1.printStackTrace();
        }

        Button saveButton = new Button("Сохранить");
        customizeButton(saveButton);
        //saveButton.setOnAction(event -> handleSaveRegistrationAction(registration)); // Используем лямбда-выражение
        registrationLayout.getChildren().add(saveButton); // Добавляем кнопку "Сохранить"

        Scene registrationScene = new Scene(registrationLayout, 300, 400);  // Создаем сцену
        registrationStage.setScene(registrationScene);  // Устанавливаем сцену для окна
        registrationStage.show();  // Отображаем окно
        System.out.println("Кнопка 'Регистрация' нажата!");
        // Создаем кнопку "Сохранить" (если нужно сохранять данные)

        saveButton.setOnAction(e -> {
            registration.add(fioField.getText());
            registration.add(carModelField.getText());
            registration.add(licensePlateField.getText());
            registration.add(registrationTime);
            registration.add(freeSpaceField.getText());
            // Закрываем окно после сохранения
            registrationStage.close();
            try{
                post_new_client(registration);
            } catch (Exception e_1) {
                e_1.printStackTrace();
            }
            System.out.println("Кнопка 'Сохранить' нажата!");
        });
        // return registration;
        // Добавьте здесь код для обработки нажатия кнопки "Регистрация"
    }

    public static List<String> handleDepartureAction() {

        List<String> departure = new ArrayList<>();
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
            String licensePlate = licensePlateField.getText();
            departure.add(departureTime);
            departure.add(licensePlateField.getText());
            System.out.println(departure);


//            // Здесь должна быть логика получения времени въезда из базы данных
//            // и расчета времени пребывания на парковке.
//            LocalDateTime entryTime = getEntryTimeFromDatabase(licensePlate); // Пример
//
//            if (entryTime == null) {
//                // Обработка случая, когда автомобиль с таким номером не найден
//                Alert alert = new Alert(Alert.AlertType.ERROR, "Автомобиль с таким номером не найден!");
//                alert.showAndWait();
//                return;
//            }
//
//            LocalDateTime exitTime = LocalDateTime.now();
//            Duration duration = Duration.between(entryTime, exitTime);
//
//            // Расчет стоимости (пример: 50 рублей в час)
//            double hourlyRate = 50.0;
//            double totalCost = (duration.toMinutes() / 60.0) * hourlyRate;
//
//            // Создание окна для отображения цены
//            Stage priceStage = new Stage();
//            priceStage.setTitle("Стоимость парковки");
//
//            Label priceLabel = new Label("К оплате: " + String.format("%.2f", totalCost) + " рублей");
//            VBox priceLayout = new VBox(10, priceLabel);
//            priceLayout.setAlignment(Pos.CENTER);
//            priceLayout.setPadding(new Insets(20));
//
//            Scene priceScene = new Scene(priceLayout, 250, 100);
//            priceStage.setScene(priceScene);
//            priceStage.show();
            try{
                bd.post_old_client(departure);
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

        return departure;
        // Добавьте здесь код для обработки нажатия кнопки "Выезд"
    }

    // Заглушка для получения времени въезда из базы данных
    private LocalDateTime getEntryTimeFromDatabase(String licensePlate) {
        // Здесь должна быть логика подключения к базе данных и получения времени въезда
        // по номеру машины.  В данном примере возвращается фиксированное время.
        if ("А123БВ77".equals(licensePlate)) { //пример с номером
            return LocalDateTime.of(2024, 1, 1, 10, 0); // Пример: 1 января 2024, 10:00
        } else {
            return null; // Если номер не найден
        }
    }

    private void handleCarHistoryAction(String car_number) {
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
            stage.setTitle("История парковки");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Ошибка: " + e.getMessage()).show();
        }
    }

    private void handleHistoryAction() {
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
            table.getItems().setAll(getHistory());
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
            String licensePlate = searchField.getText();  // Получаем номер и приводим к верхнему регистру
            // #todo
            //handleCarHistoryAction(licensePlate);

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

    public static void main(String[] args) {
        try{
            bd.bd_main();
        } catch (Exception e) {
            e.printStackTrace();
        }

        launch(args);
    }
}
