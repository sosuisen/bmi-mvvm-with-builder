package com.example.ui.view.main;

import java.util.Optional;

import com.example.model.BmiService;
import com.example.model.domain.BmiRecord;
import com.example.model.domain.Obesity;
import com.example.model.domain.unit.Units;
import com.example.model.repository.RepositoryException;
import com.example.ui.view.WindowManager;
import com.example.ui.view.common.AlertDialog;
import com.example.ui.view.settings.SettingsView;

import io.github.sosuisen.jfxbuilder.graphics.StageBuilder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class MainViewModel {
    private final BmiService bmiService;
    private final WindowManager windowManager;

    private final ObservableList<BmiRecord> bmiList = FXCollections.observableArrayList();

    private final ObjectProperty<Units> units = new SimpleObjectProperty<>();

    // SI unit value
    private final DoubleProperty mHeight = new SimpleDoubleProperty();
    private final DoubleProperty kgWeight = new SimpleDoubleProperty();

    // User input value using some unit system
    private final DoubleProperty inputHeight = new SimpleDoubleProperty();
    private final DoubleProperty inputWeight = new SimpleDoubleProperty();

    // BMI
    private final ObjectProperty<Optional<Double>> bmi = new SimpleObjectProperty<>(Optional.empty());
    private final ObjectProperty<Optional<String>> obesity = new SimpleObjectProperty<>(Optional.empty());

    public DoubleProperty heightProperty() {
        return inputHeight;
    }

    public DoubleProperty weightProperty() {
        return inputWeight;
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

    public MainViewModel(BmiService service, WindowManager windowManager, Units units) {
        this.bmiService = service;
        this.windowManager = windowManager;

        this.units.set(units);

        try {
            bmiList.setAll(service.loadBmiRecords());
        } catch (RepositoryException e) {
            AlertDialog.showError(e);
        }

        bmi.bind(Bindings.createObjectBinding(
                () -> service.calculateBmi(mHeight.get(), kgWeight.get()),
                kgWeight, mHeight));

        mHeight.bind(inputHeight.map(value -> units.convertHeightToSI(value.doubleValue())));
        kgWeight.bind(inputWeight.map(value -> units.convertWeightToSI(value.doubleValue())));

        this.units.subscribe(newValue -> {
            inputHeight.set(newValue.convertHeightFromSI(mHeight.get()));
            inputWeight.set(newValue.convertWeightFromSI(kgWeight.get()));
        });

        obesity.bind(
                bmi.map(opt -> opt.map(Obesity::getCategory)
                        .map(Obesity.Category::toString)
                        .map(String::toLowerCase)));

    }

    protected void saveBmiRecord() {
        bmi.get().ifPresent(bmi -> {
            try {
                var newRecord = bmiService.saveBmi(bmi);
                bmiList.addFirst(newRecord);
            } catch (RepositoryException e) {
                AlertDialog.showError(e);
            }
        });
    }

    protected void openSettingsWindow() {
        windowManager.showWindow(SettingsView.class, new Stage());
    }

}
