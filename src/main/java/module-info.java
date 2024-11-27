module com.example.mytunes {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jaudiotagger;
    requires java.logging;
    requires javafx.graphics;


    opens com.example.mytunes to javafx.fxml;
    exports com.example.mytunes;
}