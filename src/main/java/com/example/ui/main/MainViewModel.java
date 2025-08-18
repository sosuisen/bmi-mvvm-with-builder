package com.example.ui.main;

import java.util.Optional;

import com.example.model.BmiService;
import com.example.model.domain.BmiRecord;
import com.example.model.domain.Obesity;
import com.example.model.domain.unit.Units;
import com.example.model.repository.RepositoryException;
import com.example.ui.AlertDialog;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainViewModel {

    private final ObservableList<BmiRecord> bmiList = FXCollections.observableArrayList();

    private final ObjectProperty<Units> units = new SimpleObjectProperty<>();

    // SI unit value
    private final DoubleProperty mHeight = new SimpleDoubleProperty();
    private final DoubleProperty kgWeight = new SimpleDoubleProperty();

    // Converted value using some unit system
    private final DoubleProperty displayHeight = new SimpleDoubleProperty();
    private final DoubleProperty displayWeight = new SimpleDoubleProperty();

    // BMI
    private final ObjectProperty<Optional<Double>> bmi = new SimpleObjectProperty<>(Optional.empty());
    private final ObjectProperty<Optional<String>> obesity = new SimpleObjectProperty<>(Optional.empty());

    public DoubleProperty heightProperty() {
        return displayHeight;
    }

    public DoubleProperty weightProperty() {
        return displayWeight;
    }

    public ObjectProperty<Optional<Double>> bmiProperty() {
        return bmi;
    }

    public ObjectProperty<Optional<String>> obesityProperty() {
        return obesity;
    }

    public ObjectProperty<Units> unitsProperty() {
        return units;
    }

    public ObservableList<BmiRecord> getBmiList() {
        return bmiList;
    }

    public MainViewModel(BmiService service, Units units) {
        this.units.set(units);

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
                        newValue -> mHeight.set(units.convertHeightToSI(newValue.doubleValue())));

        displayWeight
                .subscribe(
                        newValue -> kgWeight.set(units.convertWeightToSI(newValue.doubleValue())));

        this.units.subscribe(newValue -> {
            displayHeight.set(newValue.convertHeightFromSI(mHeight.get()));
            displayWeight.set(newValue.convertWeightFromSI(kgWeight.get()));
        });

        obesity.bind(
                bmi.map(opt -> opt.map(Obesity::getCategory)
                        .map(Obesity.Category::toString)
                        .map(String::toLowerCase)));
    }

}
