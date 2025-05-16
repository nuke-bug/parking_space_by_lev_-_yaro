module org.example.parking_space {
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

    opens org.example.frontend to javafx.fxml;
    exports org.example.frontend;
}