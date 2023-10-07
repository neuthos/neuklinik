package com.example.klinikgui.controllers;

import com.example.klinikgui.models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PatientController {
    private final ObservableList<Patient> patients = FXCollections.observableArrayList();

    public boolean addPatient(Patient patient) {
        if (patient.isNikUnique(patients)) {
            patients.add(patient);
            return true;
        }
        return false;
    }

    public ObservableList<Patient> getPatients() {
        return patients;
    }


    public boolean updatePatient(Patient oldPatient, Patient newPatient) {
        if (patients.contains(oldPatient)) {
            int index = patients.indexOf(oldPatient);
            patients.set(index, newPatient);
            return true;
        }
        return false;
    }


    public boolean deletePatient(Patient patient) {
        patients.remove(patient);
        return true;
    }
}
