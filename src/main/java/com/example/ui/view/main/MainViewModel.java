package com.example.ui.view.main;

import java.util.Optional;

import com.example.model.domain.BmiRecord;
import com.example.model.domain.Obesity;
import com.example.model.repository.RepositoryException;
import com.example.model.service.BmiService;
import com.example.ui.view.CommonViewModel;
import com.example.ui.view.WindowManager;
import com.example.ui.view.alert.AlertDialog;
import com.example.ui.view.settings.SettingsView;

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

    public ObservableList<BmiRecord> getBmiList() {
        return bmiList;
    }

    public MainViewModel(BmiService service, WindowManager windowManager, CommonViewModel commonViewModel) {
        this.bmiService = service;
        this.windowManager = windowManager;

        try {
            bmiList.setAll(service.loadBmiRecords());
        } catch (RepositoryException e) {
            AlertDialog.showError(e);
        }

        bmi.bind(Bindings.createObjectBinding(
                () -> service.calculateBmi(mHeight.get(), kgWeight.get()),
                kgWeight, mHeight));

        mHeight.bind(inputHeight.map(value -> commonViewModel.convertHeightToSI(value.doubleValue())));
        kgWeight.bind(inputWeight.map(value -> commonViewModel.convertWeightToSI(value.doubleValue())));

        commonViewModel.unitsProperty().subscribe(newValue -> {
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
