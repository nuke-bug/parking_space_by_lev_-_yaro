package org.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        /*
        Conn();
        CreateDB();
        WriteDB();
        ReadDB();
        getHistory();
        CloseDB();
         */

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


        statmt.execute("CREATE TABLE if not exists 'cars' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'owner' text," +  //владелец (ФИО)
                "'car_number' text," +  // Номер машины
                "'car_brand' text" +  // Марка
                ");");
        System.out.println("Таблица cars создана");


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

    public static void Write_History() throws SQLException {
        statmt.execute("INSERT INTO history (owner, number, car_number, car_brand, check_in_time, departure_time, payment)" +
                " VALUES (2, 'Velh', '89DDD09', 'BMV', '22:33 18.05.2025', 'не выехал', '0');");
        statmt.execute("INSERT INTO history ('number', 'owner', 'car_number', 'car_brand'," +
                "check_in_time, 'departure_time', 'payment') " +
                "VALUES (2, 'Velh', '89DDD09', 'BMV', '22:33 18.05.2025', 'не выехал', '0');");
    }
    public static void WriteDB() throws SQLException {
        statmt.execute("INSERT INTO parking_spaces ('type_of_place', 'number', 'busyness', 'cost')" +
                "VALUES ('инвалидное', 1, 'свободно', 100);");
        statmt.execute("INSERT INTO parking_spaces ('type_of_place', 'number', 'busyness', 'cost')" +
                "VALUES ('универсальное', 2, 'свободно', 200);");  // Исправлено '468rt4' на число
        statmt.execute("INSERT INTO parking_spaces ('type_of_place', 'number', 'busyness', 'cost')" +
                "VALUES ('обрезанное(мотоцикл)', 3, 'занято', 300);");
        System.out.println("Таблица parking_spaces заполнена");
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

                System.out.println(
                        rs.getString("owner")+
                        rs.getString("car_number")+
                        rs.getString("car_brand")+
                        rs.getString("check_in_time")+
                        rs.getString("departure_time")+
                        rs.getString("payment"));
            }
        }
        return history;
    }


    public static void ReadDB() throws ClassNotFoundException, SQLException {
        resSet = statmt.executeQuery("SELECT * FROM history");
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }

}
