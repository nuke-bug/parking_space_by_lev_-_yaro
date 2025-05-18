package org.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class bd {
    private static Connection conn;
    private static Statement statmt;
    private static ResultSet resSet;

    public static void Conn() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:parking.sqlite3");
        System.out.println("База подключена!");
    }
    public static void bd_main() throws Exception {
        Conn();
        CreateDB();
        WriteDB();
        ReadDB();
        CloseDB();
        if (conn != null) conn.close();
    }
    public static void main(String[] args) throws Exception {
        Conn();
        CreateDB();
        WriteDB();
        ReadDB();
        CloseDB();

        // Не забудьте закрыть соединение когда оно больше не нужно
        if (conn != null) conn.close();
    }

    public static void CreateDB() throws ClassNotFoundException, SQLException {
        statmt = conn.createStatement();
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
                "'сheck_in_time' text," +  // время заезда
                "'departure_time' text," +  // время выезда ("не покидал территорию" по умолчанию)
                "'payment' text" +  // оплата(0 по умолчанию)
                ");");
        System.out.println("Таблица history создана");
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


    public static void get_history() throws ClassNotFoundException, SQLException {
        resSet = statmt.executeQuery("SELECT * FROM history");

        while (resSet.next()) {
            int number = resSet.getInt("number");
            String owner = resSet.getString("owner");
            String car_number = resSet.getString("car_number");
            String car_brand = resSet.getString("car_brand");
            String сheck_in_time = resSet.getString("сheck_in_time");
            String departure_time = resSet.getString("departure_time");
            String payment = resSet.getString("payment");
            /*System.out.print(" ID = " + id);
            System.out.print(" name = " + name);
            System.out.print(" phone = " + phone);
            System.out.println();*/

        }

        System.out.println("Таблица выведена");
    }


    public static void ReadDB() throws ClassNotFoundException, SQLException {
        resSet = statmt.executeQuery("SELECT * FROM users");

        while (resSet.next()) {
            int id = resSet.getInt("id");
            String name = resSet.getString("name");
            String phone = resSet.getString("phone");
            System.out.println("ID = " + id);
            System.out.println("name = " + name);
            System.out.println("phone = " + phone);
            System.out.println();

        }

        System.out.println("Таблица выведена");
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();

        System.out.println("Соединения закрыты");
    }

}
