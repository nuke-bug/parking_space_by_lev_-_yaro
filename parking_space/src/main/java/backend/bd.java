package backend;

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
        conn = DriverManager.getConnection("jdbc:sqlite:TEST1.sqlite3");
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
        statmt.execute("CREATE TABLE if not exists 'users' (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'name' text," +
                "'phone' INT" +  // Убрана лишняя запятая
                ");");
        System.out.println("Таблица создана");
    }

    public static void WriteDB() throws SQLException {
        statmt.execute("INSERT INTO users ('name', 'phone') VALUES ('Petya', 125453);");
        statmt.execute("INSERT INTO users ('name', 'phone') VALUES ('Vasya', 468744);");  // Исправлено '468rt4' на число
        statmt.execute("INSERT INTO users ('name', 'phone') VALUES ('Masha', 872346);");
        System.out.println("Таблица заполнена");
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
