package com.example.ui.main;

import java.util.Optional;

import com.example.model.BmiService;
import com.example.model.domain.BmiCalculator;
import com.example.model.domain.BmiRecord;
import com.example.model.domain.Unit;
import com.example.model.repository.RepositoryException;
import com.example.ui.AlertDialog;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainViewModel {
    private final BmiService service;

    private final ObservableList<BmiRecord> bmiList = FXCollections.observableArrayList();

    private final ObjectProperty<Unit.UnitSystem> unitSystem = new SimpleObjectProperty<>(Unit.UnitSystem.SI);

    // SI unit value
    private final DoubleProperty mHeight = new SimpleDoubleProperty();
    private final DoubleProperty kgWeight = new SimpleDoubleProperty();

    // Converted value using some unit system
    private final DoubleProperty displayHeight = new SimpleDoubleProperty();
    private final DoubleProperty displayWeight = new SimpleDoubleProperty();

    // BMI
    private final ObjectProperty<Optional<Double>> bmi = new SimpleObjectProperty<>(Optional.empty());
    private final StringProperty obesity = new SimpleStringProperty();

    public DoubleProperty heightProperty() {
        return displayHeight;
    }

    public DoubleProperty weightProperty() {
        return displayWeight;
    }

    public ObjectProperty<Optional<Double>> bmiProperty() {
        return bmi;
    }

    public StringProperty obesityProperty() {
        return obesity;
    }

    public ObjectProperty<Unit.UnitSystem> unitSystemProperty() {
        return unitSystem;
    }

    public ObservableList<BmiRecord> getBmiList() {
        return bmiList;
    }

    public MainViewModel(BmiService service) {
        this.service = service;
        try {
            bmiList.setAll(service.loadBmiRecords());
        } catch (RepositoryException e) {
            AlertDialog.showError(e);
        }

        bmi.bind(Bindings.createObjectBinding(
                () -> service.calculateBmi(mHeight.get(), kgWeight.get()),
                kgWeight, mHeight));

        displayHeight
                .subscribe(
                        newValue -> mHeight.set(service.convertHeightToSI(unitSystem.get(), newValue.doubleValue())));

        displayWeight
                .subscribe(
                        newValue -> kgWeight.set(service.convertWeightToSI(unitSystem.get(), newValue.doubleValue())));

        unitSystem.subscribe(newValue -> {
            displayHeight.set(service.convertHeightFromSI(newValue, mHeight.get()));
            displayWeight.set(service.convertWeightFromSI(newValue, kgWeight.get()));
        });
    }

}
