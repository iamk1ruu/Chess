module com.kiruu.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jlama.core;
    requires org.checkerframework.checker.qual;


    opens com.kiruu.chess to javafx.fxml;
    exports com.kiruu.chess;
}