package com.example.ui.view;

import com.example.model.domain.unit.Units;
import com.example.model.repository.RepositoryException;
import com.example.model.service.ConfigService;
import com.example.ui.view.alert.AlertDialog;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CommonViewModel {
    private final ObjectProperty<Units> units = new SimpleObjectProperty<>();
    private final ObjectProperty<String> language = new SimpleObjectProperty<>();

    public CommonViewModel(ConfigService configService) {
        try {
            units.set(configService.getUnits());
            language.set(configService.getLanguage());
        } catch (RepositoryException e) {
            AlertDialog.showError(e);
        }

        units.addListener((_, _, newValue) -> {
            try {
                configService.setUnits(newValue);
            } catch (RepositoryException e) {
                AlertDialog.showError(e);
            }
        });

        language.addListener((_, _, newValue) -> {
            try {
                configService.setLanguage(newValue);
            } catch (RepositoryException e) {
                AlertDialog.showError(e);
            }
        });
    }

    public ObjectProperty<Units> unitsProperty() {
        return units;
    }

    public double convertHeightFromSI(double value) {
        return units.get().convertHeightFromSI(value);
    }

    public double convertHeightToSI(double value) {
        return units.get().convertHeightToSI(value);
    }

    public double convertWeightFromSI(double value) {
        return units.get().convertWeightFromSI(value);
    }

    public double convertWeightToSI(double value) {
        return units.get().convertWeightToSI(value);
    }

}