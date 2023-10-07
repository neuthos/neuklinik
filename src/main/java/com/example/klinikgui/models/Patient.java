package com.example.klinikgui.models;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Patient {
    private final StringProperty name;
    private final StringProperty address;
    private final LongProperty nik;
    private final ObjectProperty<LocalDate> birthDate;

    public Patient() {
        this(null, null, 0L, null);
    }

    public Patient(String name, String address, long nik, LocalDate birthDate) {
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.nik = new SimpleLongProperty(nik);
        this.birthDate = new SimpleObjectProperty<>(birthDate);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public long getNik() {
        return nik.get();
    }

    public void setNik(long nik) {
        this.nik.set(nik);
    }

    public LongProperty nikProperty() {
        return nik;
    }

    public LocalDate getBirthDate() {
        return birthDate.get();
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate.set(birthDate);
    }

    public ObjectProperty<LocalDate> birthDateProperty() {
        return birthDate;
    }

    public boolean isNikUnique(ObservableList<Patient> patientList) {
        return patientList.stream().noneMatch(patient -> patient.getNik() == this.getNik());
    }

    public String formattedBirthDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        return birthDate.get().format(formatter);
    }
}
