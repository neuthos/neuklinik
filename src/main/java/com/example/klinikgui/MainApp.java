package com.example.klinikgui;

import com.example.klinikgui.controllers.PatientController;
import com.example.klinikgui.models.Patient;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

public class MainApp extends Application {
    private final PatientController patientController = new PatientController();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        TableView<Patient> tableView = new TableView<>();

        TableColumn<Patient, Integer> noColumn = new TableColumn<>("No");
        noColumn.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(column.getValue()) + 1));

        TableColumn<Patient, String> namaColumn = new TableColumn<>("Nama Pasien");
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Patient, Long> nikColumn = new TableColumn<>("NIK");
        nikColumn.setCellValueFactory(new PropertyValueFactory<>("nik"));

        TableColumn<Patient, LocalDate> tanggalLahirColumn = new TableColumn<>("Tanggal Lahir");
        tanggalLahirColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        TableColumn<Patient, String> alamatColumn = new TableColumn<>("Alamat");
        alamatColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Patient, Void> actionColumn = new TableColumn<>("Aksi");
        actionColumn.setPrefWidth(250);
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            {
                updateButton.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());

                    TextField nameField = new TextField();
                    TextField addressField = new TextField();
                    TextField nikField = new TextField();
                    DatePicker birthDatePicker = new DatePicker();

                    nameField.setText(patient.getName());
                    addressField.setText(patient.getAddress());
                    nikField.setText(String.valueOf(patient.getNik()));
                    birthDatePicker.setValue(patient.getBirthDate());

                    VBox formLayout = new VBox(10);
                    formLayout.getChildren().addAll(
                            new Label("Nama Pasien:"),
                            nameField,
                            new Label("Alamat:"),
                            addressField,
                            new Label("NIK:"),
                            nikField,
                            new Label("Tanggal Lahir:"),
                            birthDatePicker
                    );

                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.initOwner(primaryStage);
                    dialog.setTitle("Update Pasien");
                    dialog.setHeaderText("Form Update Pasien");
                    dialog.getDialogPane().setContent(formLayout);

                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    Optional<ButtonType> result = dialog.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        String name = nameField.getText();
                        String address = addressField.getText();
                        long nik = Long.parseLong(nikField.getText());
                        LocalDate birthDate = birthDatePicker.getValue();

                        Patient newPatient = new Patient(name, address, nik, birthDate);
                        boolean isSuccessUpdate = patientController.updatePatient(patient, newPatient);

                        VboxFormPatient(nameField, addressField, nikField, birthDatePicker, isSuccessUpdate, tableView);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    boolean deleted = patientController.deletePatient(patient);
                    if (deleted) {
                        tableView.getItems().remove(patient);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(updateButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });


        tableView.getColumns().addAll(noColumn, namaColumn, nikColumn, tanggalLahirColumn, alamatColumn, actionColumn);


        Button addButton = new Button("Tambah Pasien");

        addButton.setOnAction(event -> {
            TextField nameField = new TextField();
            TextField addressField = new TextField();
            TextField nikField = new TextField();
            DatePicker birthDatePicker = new DatePicker();

            VBox formLayout = new VBox(10);
            formLayout.getChildren().addAll(
                    new Label("Nama Pasien:"),
                    nameField,
                    new Label("Alamat:"),
                    addressField,
                    new Label("NIK:"),
                    nikField,
                    new Label("Tanggal Lahir:"),
                    birthDatePicker
            );

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(primaryStage);
            dialog.setTitle("Tambah Pasien");
            dialog.setHeaderText("Form Tambah Pasien");
            dialog.getDialogPane().setContent(formLayout);

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();


            if (result.isPresent() && result.get() == ButtonType.OK) {
                String name = nameField.getText();
                String address = addressField.getText();
                long nik = Long.parseLong(nikField.getText());
                LocalDate birthDate = birthDatePicker.getValue();

                Patient newPatient = new Patient(name, address, nik, birthDate);

                System.out.println(newPatient.getName());
                boolean added = patientController.addPatient(newPatient);
                System.out.println(added);
                VboxFormPatient(nameField, addressField, nikField, birthDatePicker, added, tableView);
            }
        });



        ObservableList<Patient> patientList = patientController.getPatients();
        tableView.setItems(patientList);


        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));

        mainLayout.getChildren().addAll(addButton, tableView);

        Scene scene = new Scene(mainLayout, 800, 600);

        primaryStage.setTitle("Aplikasi NeuKlinik");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void VboxFormPatient(TextField nameField, TextField addressField, TextField nikField, DatePicker birthDatePicker, boolean isSuccessUpdate, TableView<Patient> tableView) {
        if (isSuccessUpdate) {
            nameField.clear();
            addressField.clear();
            nikField.clear();
            birthDatePicker.setValue(null);
            tableView.setItems(patientController.getPatients());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("NIK sudah ada");
            alert.setContentText("Pasien dengan NIK tersebut sudah ada.");
            alert.showAndWait();
        }
    }
}
