package org.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.frontend.FronEnd;
import org.frontend.FronEnd.*;

public class bd {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    public static void Conn() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:parking.sqlite3");
        statmt = conn.createStatement();
        System.out.println("База подключена!");
    }

    public static void bd_main() throws Exception {
        Conn();
    }

    public static void main(String[] args) throws Exception {
        Conn();
        CreateDB();
        WriteDB();
        Write_History();
        CloseDB();
        if (conn != null) conn.close();

        // Не забудьте закрыть соединение когда оно больше не нужно
    }

    public static void CreateDB() throws ClassNotFoundException, SQLException {
        statmt.execute("CREATE TABLE if not exists 'parking_spaces' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'type_of_place' text," +  //тип места
                "'number' INT," +  // Номер места
                "'busyness' text," +  // занято или свободно (занято/свободно)
                "'cost' INT" +  // стоимость в час
                ");");
        System.out.println("Таблица parking_spaces создана");

        statmt.execute("CREATE TABLE if not exists 'history' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'owner' text," +  //владелец (фио)
                "'number' INT," +  // Номер места
                "'car_number' text," +  // Номер машины
                "'car_brand' text," +  // Марка
                "'check_in_time' text," +  // время заезда
                "'departure_time' text," +  // время выезда ("не покидал территорию" по умолчанию)
                "'payment' text" +  // оплата(0 по умолчанию)
                ");");
        System.out.println("Таблица history создана");
    }

    public static List<ParkingHistory> find_in_history(String car_number) throws SQLException {
        List<ParkingHistory> car_history = new ArrayList<>();
        try (ResultSet rs = statmt.executeQuery("SELECT * FROM history where car_number = '" + car_number + "'")) {
            while (rs.next()) {
                car_history.add(new ParkingHistory(
                        rs.getInt("number"),
                        rs.getString("owner"),
                        rs.getString("car_number"),
                        rs.getString("car_brand"),
                        rs.getString("check_in_time"),
                        rs.getString("departure_time"),
                        rs.getString("payment")
                ));
            }
        }
        System.out.println(car_history);
        return car_history;
    }

    public static List<ParkingHistory> get_history_this_day() throws SQLException {
        List<ParkingHistory> car_history = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = now.format(formatter);
        System.out.println(formattedDateTime);
        try (ResultSet rs = statmt.executeQuery("SELECT * FROM history where check_in_time like '%" + formattedDateTime + "%';")) {
            // "*' or departure_time like '" + formattedDateTime + "*'")) {
            while (rs.next()) {
                car_history.add(new ParkingHistory(
                        rs.getInt("number"),
                        rs.getString("owner"),
                        rs.getString("car_number"),
                        rs.getString("car_brand"),
                        rs.getString("check_in_time"),
                        rs.getString("departure_time"),
                        rs.getString("payment")
                ));
            }
        }
        return car_history;
    }

    public static List<ParkingSpace> get_free_places() throws SQLException {
        List<ParkingSpace> free_parking_spaces = new ArrayList<>();
        try (ResultSet rs = statmt.executeQuery("SELECT * FROM parking_spaces where busyness = 'свободно';")) {
            while (rs.next()) {
                free_parking_spaces.add(new ParkingSpace(
                        rs.getInt("number"),
                        rs.getString("type_of_place"),
                        rs.getString("busyness"),
                        rs.getString("cost")
                ));
            }
        }
        return free_parking_spaces;
    }

    public static List<ParkingSpace> get_occupied_places() throws SQLException {
        List<ParkingSpace> free_parking_spaces = new ArrayList<>();
        try (ResultSet rs = statmt.executeQuery("SELECT number FROM parking_spaces where busyness = 'занято';")) {
            while (rs.next()) {
                free_parking_spaces.add(new ParkingSpace(
                        rs.getInt("number"),
                        rs.getString("type_of_place"),
                        rs.getString("busyness"),
                        rs.getString("cost")
                ));
            }
        }
        return free_parking_spaces;
    }

    public static boolean post_new_client(List<String> registration) throws SQLException {
        String owner = registration.get(0);
        String car_brand = registration.get(1);
        String car_number = registration.get(2);
        String check_in_time = registration.get(3);
        String number = registration.get(4);
        String query = "SELECT * FROM parking_spaces where number='" + number + "';";
        try (ResultSet rs = statmt.executeQuery(query)) {
            if (rs.next()) { // Проверяем есть ли результат и переходим к первой записи
                String answer = rs.getString("busyness"); // Получаем значение ПОСЛЕ перехода к записи

                // Правильное сравнение строк через equals()
                boolean isAvailable = "свободно".equals(answer);

                System.out.println("Is available: " + isAvailable);
                System.out.println("Current status: " + answer);
                if (!isAvailable){
                    return false;
                }
            }
        }

        statmt.execute("INSERT INTO history (owner, number, car_number, car_brand, check_in_time, departure_time, payment)" +
                " VALUES ('" + owner + "', " + number + ", '" + car_number + "', '" + car_brand + "', '" + check_in_time + "', 'не выехал', '0');");
        statmt.execute("UPDATE parking_spaces SET busyness = 'занято' WHERE number = " + number + " ;");

        System.out.println("True");
        return true;
    }

    public static List<String> post_old_client(List<String> registration) throws SQLException {
        String departure_time = registration.get(0);
        String car_number = registration.get(1);

        //String number = registration.get(2);
        //String delta_time = registration.get(3);

        statmt.execute("UPDATE parking_spaces SET busyness = 'свободно' WHERE number =" +
                " (select number from history where car_number = '" + car_number + "' and payment = '0');");
        statmt.execute("UPDATE history SET departure_time = '" + departure_time + "' WHERE (car_number = '" + car_number + "') " +
                " AND (payment = '0');");

        List<String> cost_and_time = new ArrayList<>();
        String query  = "SELECT cost FROM parking_spaces where number=" +
                "(select number from history WHERE (car_number = '" + car_number + "') " +
                "AND (payment = '0'))";
        try (ResultSet rs = statmt.executeQuery(query)){
            rs.next();
            cost_and_time.add(new String(rs.getString("cost")));
        }
        query  = "SELECT check_in_time FROM history WHERE (car_number = '" + car_number + "') " +
                "AND (payment = '0');";
        try (ResultSet rs = statmt.executeQuery(query)) {
            rs.next();
            cost_and_time.add(new String(rs.getString("check_in_time")));
        }
        System.out.println(cost_and_time);
        return cost_and_time;


    }

    public static void WriteDB() throws SQLException {
        statmt.execute("INSERT INTO parking_spaces ('type_of_place', 'number', 'busyness', 'cost')" +
                "VALUES ('инвалидное', 1, 'свободно', 100);");
        statmt.execute("INSERT INTO parking_spaces ('type_of_place', 'number', 'busyness', 'cost')" +
                "VALUES ('универсальное', 2, 'свободно', 200);");
        statmt.execute("INSERT INTO parking_spaces ('type_of_place', 'number', 'busyness', 'cost')" +
                "VALUES ('обрезанное(мотоцикл)', 3, 'занято', 300);");
        System.out.println("Таблица parking_spaces заполнена");
    }
    public static void Write_History() throws SQLException {
                statmt.execute("INSERT INTO history ('number', 'owner', 'car_number', 'car_brand'," +
                "check_in_time, 'departure_time', 'payment') " +
                "VALUES (2, 'Velh', '89DDD09', 'BMV', '22:33 18.05.2025', 'не выехал', '0');");
    }
    public static class ParkingHistory {
        public int number;
        public String owner;
        public String carNumber;
        public String carBrand;
        public String checkInTime;
        public String departureTime;
        public String payment;

        public ParkingHistory(int number, String owner, String carNumber,
                              String carBrand, String checkInTime,
                              String departureTime, String payment) {
            this.number = number;
            this.owner = owner;
            this.carNumber = carNumber;
            this.carBrand = carBrand;
            this.checkInTime = checkInTime;
            this.departureTime = departureTime;
            this.payment = payment;
        }

        // Только геттеры
        public int getNumber() { return number; }
        public String getOwner() { return owner; }
        public String getCarNumber() { return carNumber; }
        public String getCarBrand() { return carBrand; }
        public String getCheckInTime() { return checkInTime; }
        public String getDepartureTime() { return departureTime; }
        public String getPayment() { return payment; }
    }

    public static List<ParkingHistory> getHistory() throws SQLException {
        List<ParkingHistory> history = new ArrayList<>();
        try (ResultSet rs = statmt.executeQuery("SELECT * FROM history")) {
            while (rs.next()) {
                history.add(new ParkingHistory(
                        rs.getInt("number"),
                        rs.getString("owner"),
                        rs.getString("car_number"),
                        rs.getString("car_brand"),
                        rs.getString("check_in_time"),
                        rs.getString("departure_time"),
                        rs.getString("payment")
                ));
                /*
                System.out.println(
                        rs.getString("owner")+
                        rs.getString("car_number")+
                        rs.getString("car_brand")+
                        rs.getString("check_in_time")+
                        rs.getString("departure_time")+
                        rs.getString("payment"));
                        */
            }
        }
        return history;
    }



    public static class ParkingSpace {
        public int number;
        public String type_of_place;
        public String busyness;
        public String cost;

        public ParkingSpace(int number, String type_of_place, String busyness,
                              String cost) {
            this.number = number;
            this.type_of_place = type_of_place;
            this.busyness = busyness;
            this.cost = cost;
        }

        // Только геттеры
        public int getNumber() { return number; }
        public String getType() { return type_of_place; }
        public String getCarBusyness() { return busyness; }
        public String getCost() { return cost; }
    }

    public static List<ParkingSpace> get_parking_spaces()throws SQLException {
        List<ParkingSpace> parking_spaces = new ArrayList<>();
        try (ResultSet rs = statmt.executeQuery("SELECT * FROM parking_spaces")) {
            while (rs.next()) {
                parking_spaces.add(new ParkingSpace(
                        rs.getInt("number"),
                        rs.getString("type_of_place"),
                        rs.getString("busyness"),
                        rs.getString("cost")
                ));
            }
        }
        return parking_spaces;
    }



//    public static void ReadDB() throws ClassNotFoundException, SQLException {
//        resSet = statmt.executeQuery("SELECT * FROM history");
//    }

    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        //resSet.close();

        System.out.println("Соединения закрыты");
    }

}
