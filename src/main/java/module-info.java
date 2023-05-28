module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.jsoup;
    requires okhttp;
    requires selenium.api;
    requires selenium.chrome.driver;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}