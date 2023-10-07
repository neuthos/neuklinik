module com.example.klinikgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.klinikgui to javafx.fxml;
    exports com.example.klinikgui;
    exports com.example.klinikgui.models;
    exports com.example.klinikgui.controllers;
}