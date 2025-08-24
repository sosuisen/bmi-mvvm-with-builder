package com.example.presentation.view.main;

import java.time.LocalDate;
import java.util.Optional;

import com.example.domain.model.ObesityCategory;
import com.example.domain.model.unit.UnitSystem;
import com.example.domain.service.BmiRecordWithDiff;
import com.example.presentation.view.WindowManager;
import com.example.presentation.view.application.BmiCommonAppModel;
import com.example.presentation.view.application.ConfigAppModel;
import com.example.presentation.view.settings.SettingsView;

import io.github.sosuisen.jfxbuilder.graphics.StageBuilder;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.stage.Stage;

public class MainViewModel {
    private static final Point2D OFFSET_POSITION_OF_NEW_WINDOW = new Point2D(30, 30);

    private final WindowManager windowManager;

    private final BmiCommonAppModel bmiCommonAppModel;
    private final ConfigAppModel configAppModel;

    // SI unit value
    private final DoubleProperty heightMeter = new SimpleDoubleProperty();
    private final DoubleProperty weightKg = new SimpleDoubleProperty();

    // User input value using some unit system
    private final DoubleProperty inputHeight = new SimpleDoubleProperty();
    private final DoubleProperty inputWeight = new SimpleDoubleProperty();

    // BMI
    private final ObjectProperty<Optional<Double>> bmi = new SimpleObjectProperty<>(Optional.empty());
    private final ObjectProperty<Optional<String>> obesity = new SimpleObjectProperty<>(Optional.empty());

    // Date
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(LocalDate.now());

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

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public ObservableList<BmiRecordWithDiff> getBmiList() {
        return bmiCommonAppModel.getBmiList();
    }

    public ObjectProperty<UnitSystem> unitSystemProperty() {
        return configAppModel.unitSystemProperty();
    }

    public ObjectProperty<Throwable> errorProperty() {
        return bmiCommonAppModel.errorProperty();
    }

    public MainViewModel(WindowManager windowManager,
            BmiCommonAppModel commonViewModel, ConfigAppModel configAppModel) {
        this.windowManager = windowManager;
        this.configAppModel = configAppModel;
        this.bmiCommonAppModel = commonViewModel;

        bmi.bind(bmiCommonAppModel.getBmiBinding(heightMeter, weightKg));

        heightMeter.bind(inputHeight
                .map(value -> configAppModel.unitSystemProperty().get().convertHeightToSI(value.doubleValue())));

        weightKg.bind(inputWeight
                .map(value -> configAppModel.unitSystemProperty().get().convertWeightToSI(value.doubleValue())));

        var latestRecord = commonViewModel.getBmiList().size() > 0
                ? commonViewModel.getBmiList().get(0)
                : null;

        inputHeight.set(latestRecord != null
                ? configAppModel.unitSystemProperty().get().convertHeightFromSI(latestRecord.heightMeter())
                : 0.0);
        inputWeight.set(latestRecord != null
                ? configAppModel.unitSystemProperty().get().convertWeightFromSI(latestRecord.weightKg())
                : 0.0);

        configAppModel.unitSystemProperty().subscribe(newValue -> {
            inputHeight.set(newValue.convertHeightFromSI(heightMeter.get()));
            inputWeight.set(newValue.convertWeightFromSI(weightKg.get()));
        });

        obesity.bind(
                bmi.map(opt -> opt.map(ObesityCategory::getCategory)
                        .map(ObesityCategory::toResourceString)));

    }

    public void saveBmiRecord() {
        bmi.get().ifPresent(_ -> {
            bmiCommonAppModel.saveRecord(heightMeter.get(), weightKg.get(), date.get());
        });
    }

    public double convertHeightFromSI(double height) {
        return unitSystemProperty().get().convertHeightFromSI(height);
    }

    public double convertWeightFromSI(double weight) {
        return unitSystemProperty().get().convertWeightFromSI(weight);
    }

    public void removeRecord(int id) {
        bmiCommonAppModel.removeRecord(id);
    }

    public void setToday() {
        date.set(LocalDate.now());
    }

    public void openSettingsWindow(Stage currentStage) {
        Point2D newPosition;
        if (currentStage.getX() > SettingsView.WIDTH) {
            newPosition = new Point2D(currentStage.getX() - SettingsView.WIDTH, currentStage.getY());
        } else {
            newPosition = new Point2D(currentStage.getX(), currentStage.getY()).add(OFFSET_POSITION_OF_NEW_WINDOW);
        }
        var newStage = StageBuilder.create()
                .x(newPosition.getX())
                .y(newPosition.getY())
                .build();

        windowManager.showWindow(SettingsView.class, newStage);
    }

}
