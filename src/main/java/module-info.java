module com.example.electionsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires eu.hansolo.tilesfx;
    requires jdk.compiler;
    requires java.sql;

    opens com.example.electionsystem to javafx.fxml;
    exports com.example.electionsystem;
}