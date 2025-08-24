package com.example.presentation.view.application;

import java.time.LocalDate;
import java.util.Optional;

import com.example.domain.exception.RepositoryException;
import com.example.domain.service.BmiRecordWithDiff;
import com.example.domain.service.BmiService;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Manage current BMI list and its reload logic.
 */
public class BmiCommonAppModel {
    private final BmiService bmiService;

    private final ObservableList<BmiRecordWithDiff> bmiList = FXCollections.observableArrayList();

    private final ObjectProperty<Throwable> error = new SimpleObjectProperty<>();

    public ObjectProperty<Throwable> errorProperty() {
        return error;
    }

    public ObservableList<BmiRecordWithDiff> getBmiList() {
        return bmiList;
    }

    public BmiCommonAppModel(BmiService bmiService) {
        this.bmiService = bmiService;
        reloadRecords();
    }

    public ObjectBinding<Optional<Double>> getBmiBinding(DoubleProperty heightMeter, DoubleProperty weightKg) {
        return Bindings.createObjectBinding(
                () -> bmiService.calculateBmi(heightMeter.get(), weightKg.get()),
                heightMeter, weightKg);
    }

    public void saveRecord(double heightMeter, double weightKg, LocalDate date) {
        try {
            bmiService.upsertRecord(heightMeter, weightKg, date);
            // Must reload to handle proper diff
            reloadRecords();
        } catch (RepositoryException e) {
            errorProperty().set(e);
        }
    }

    public void removeRecord(int id) {
        try {
            bmiService.removeRecord(id);
            reloadRecords();
        } catch (RepositoryException e) {
            errorProperty().set(e);
        }
    }

    public void removeAllRecords() {
        try {
            bmiService.removeAllRecords();
            reloadRecords();
        } catch (RepositoryException e) {
            errorProperty().set(e);
        }
    }

    protected void reloadRecords() {
        try {
            bmiList.setAll(bmiService.loadRecords());
        } catch (RepositoryException e) {
            error.set(e);
        }
    }

}