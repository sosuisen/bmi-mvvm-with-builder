package com.example.presentation.view.main;

import java.util.Optional;

import com.example.domain.model.BmiRecord;
import com.example.domain.model.ObesityCategory;
import com.example.domain.model.unit.UnitSystem;
import com.example.domain.exception.RepositoryException;
import com.example.domain.service.BmiService;
import com.example.presentation.view.WindowManager;
import com.example.presentation.view.common.CommonViewModel;
import com.example.presentation.view.settings.SettingsView;

import io.github.sosuisen.jfxbuilder.graphics.StageBuilder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.stage.Stage;

public class MainViewModel {
    private static final Point2D OFFSET_POSITION_OF_NEW_WINDOW = new Point2D(30, 30);

    private final BmiService bmiService;
    private final WindowManager windowManager;

    private final CommonViewModel commonViewModel;

    // SI unit value
    private final DoubleProperty heightMeter = new SimpleDoubleProperty();
    private final DoubleProperty weightKg = new SimpleDoubleProperty();

    // User input value using some unit system
    private final DoubleProperty inputHeight = new SimpleDoubleProperty();
    private final DoubleProperty inputWeight = new SimpleDoubleProperty();

    // BMI
    private final ObjectProperty<Optional<Double>> bmi = new SimpleObjectProperty<>(Optional.empty());
    private final ObjectProperty<Optional<String>> obesity = new SimpleObjectProperty<>(Optional.empty());

    protected DoubleProperty heightProperty() {
        return inputHeight;
    }

    protected DoubleProperty weightProperty() {
        return inputWeight;
    }

    protected ObjectProperty<Optional<Double>> bmiProperty() {
        return bmi;
    }

    protected ObjectProperty<Optional<String>> obesityProperty() {
        return obesity;
    }

    protected ObservableList<BmiRecord> getBmiList() {
        return commonViewModel.getBmiList();
    }

    protected ObjectProperty<UnitSystem> unitSystemProperty() {
        return commonViewModel.unitSystemProperty();
    }

    protected ObjectProperty<Throwable> errorProperty() {
        return commonViewModel.errorProperty();
    }

    public MainViewModel(BmiService service, WindowManager windowManager, CommonViewModel commonViewModel) {
        this.bmiService = service;
        this.windowManager = windowManager;
        this.commonViewModel = commonViewModel;

        bmi.bind(Bindings.createObjectBinding(
                () -> service.calculateBmi(heightMeter.get(), weightKg.get()),
                weightKg, heightMeter));

        heightMeter.bind(inputHeight
                .map(value -> commonViewModel.unitSystemProperty().get().convertHeightToSI(value.doubleValue())));

        weightKg.bind(inputWeight
                .map(value -> commonViewModel.unitSystemProperty().get().convertWeightToSI(value.doubleValue())));

        commonViewModel.unitSystemProperty().subscribe(newValue -> {
            inputHeight.set(newValue.convertHeightFromSI(heightMeter.get()));
            inputWeight.set(newValue.convertWeightFromSI(weightKg.get()));
        });

        obesity.bind(
                bmi.map(opt -> opt.map(ObesityCategory::getCategory)
                        .map(ObesityCategory::toResourceString)));

    }

    protected void saveBmiRecord() {
        bmi.get().ifPresent(_ -> {
            try {
                var newRecord = bmiService.saveBmi(heightMeter.get(), weightKg.get());
                commonViewModel.getBmiList().addFirst(newRecord);
            } catch (RepositoryException e) {
                commonViewModel.errorProperty().set(e);
            }
        });
    }

    protected void openSettingsWindow(Stage currentStage) {
        var newPosition = new Point2D(currentStage.getX(), currentStage.getY()).add(OFFSET_POSITION_OF_NEW_WINDOW);

        var newStage = StageBuilder.create()
                .x(newPosition.getX())
                .y(newPosition.getY())
                .build();

        windowManager.showWindow(SettingsView.class, newStage);
    }

    protected double convertHeightFromSI(double height) {
        return unitSystemProperty().get().convertHeightFromSI(height);
    }

    protected double convertWeightFromSI(double weight) {
        return unitSystemProperty().get().convertWeightFromSI(weight);
    }

}
