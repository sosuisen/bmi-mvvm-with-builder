package com.example.presentation.view.application;

import java.time.LocalDate;
import java.util.Optional;

import com.example.domain.exception.RepositoryException;
import com.example.domain.service.BmiRecordWithDiff;
import com.example.domain.service.BmiService;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Manage current BMI list and its reload logic.
 */
public class BmiCommonAppModel {
    public static final Integer[] HISTORY_LIMIT = { 20, 50, 100 };
    // public static final Integer[] HISTORY_LIMIT = { 1, 2, 3 };

    private final BmiService bmiService;

    private final ObservableList<BmiRecordWithDiff> bmiList = FXCollections.observableArrayList();

    private final IntegerProperty historyLimit = new SimpleIntegerProperty(HISTORY_LIMIT[0]);

    private final ObjectProperty<Throwable> error = new SimpleObjectProperty<>();

    public ObjectProperty<Throwable> errorProperty() {
        return error;
    }

    public ObservableList<BmiRecordWithDiff> getBmiList() {
        return bmiList;
    }

    public IntegerProperty historyLimitProperty() {
        return historyLimit;
    }

    public BmiCommonAppModel(BmiService bmiService) {
        this.bmiService = bmiService;
        reloadRecords();
        historyLimit.subscribe(() -> reloadRecords());
    }

    public ObjectBinding<Optional<Double>> getBmiBinding(DoubleProperty heightMeter, DoubleProperty weightKg) {
        return Bindings.createObjectBinding(
                () -> bmiService.calculateBmi(heightMeter.get(), weightKg.get()),
                heightMeter, weightKg);
    }

    /**
     * Returns the latest BMI record from the list.
     *
     * @return The latest {@link BmiRecordWithDiff} if available, otherwise
     *         {@code null}.
     */
    public BmiRecordWithDiff getLatestRecrd() {
        return bmiList.size() > 0
                ? bmiList.get(0)
                : null;
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
            bmiList.setAll(bmiService.loadRecords(historyLimit.get()));
        } catch (RepositoryException e) {
            error.set(e);
        }
    }

}