module com.example.mytunes {
    requires javafx.fxml;
    requires static lombok;
    requires jaudiotagger;
    requires org.controlsfx.controls;
    requires java.logging;


    opens com.example.mytunes to javafx.fxml;
    exports com.example.mytunes;
}