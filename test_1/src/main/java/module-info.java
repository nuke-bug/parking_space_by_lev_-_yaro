module org.example.test_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.bd to javafx.fxml;
    opens org.BackEnd to javafx.fxml;
    opens org.frontend to javafx.fxml;
    exports org.frontend;
    exports org.bd;
    exports org.BackEnd;
}