package com.example.presentation.view.common;

import com.example.domain.model.Languages;
import com.example.domain.model.unit.SIUnitsWithCentimeters;
import com.example.domain.model.unit.UnitSystem;

import java.util.Locale;

import com.example.domain.exception.RepositoryException;
import com.example.domain.service.BmiRecordWithDiff;
import com.example.domain.service.BmiService;
import com.example.domain.service.ConfigService;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommonViewModel {
    private final BmiService bmiService;

    private final ObjectProperty<UnitSystem> unitSystem = new SimpleObjectProperty<>();
    private final ObjectProperty<Languages> language = new SimpleObjectProperty<>();

    private final ObservableList<BmiRecordWithDiff> bmiList = FXCollections.observableArrayList();

    private final ObjectProperty<Throwable> error = new SimpleObjectProperty<>();

    public ObjectProperty<Throwable> errorProperty() {
        return error;
    }

    public ObservableList<BmiRecordWithDiff> getBmiList() {
        return bmiList;
    }

    public CommonViewModel(BmiService bmiService, ConfigService configService) {
        this.bmiService = bmiService;
        loadBmiRecords();
        try {
            unitSystem.set(configService.getUnitSystem());
        } catch (RepositoryException e) {
            unitSystem.set(new SIUnitsWithCentimeters());
            error.set(e);
        }

        try {
            language.set(configService.getLanguage());
        } catch (RepositoryException e) {
            language.set(Languages.getDefaultLanguages());
            error.set(e);
        }

        unitSystem.addListener((_, _, newValue) -> {
            try {
                configService.setUnitSystem(newValue);
                // Reload to call a cellfactory to display converted values
                // based on the new unit system.
                loadBmiRecords();
            } catch (RepositoryException e) {
                error.set(e);
            }
        });

        language.addListener((_, _, newValue) -> {
            try {
                configService.setLanguage(newValue);
                I18n.getInstance().setResources(Locale.of(newValue.toLanguageString()));
            } catch (RepositoryException e) {
                error.set(e);
            }
        });
    }

    public void loadBmiRecords() {
        try {
            bmiList.setAll(bmiService.loadBmiRecords());
        } catch (RepositoryException e) {
            error.set(e);
        }
    }

    public ObjectProperty<UnitSystem> unitSystemProperty() {
        return unitSystem;
    }

    public ObjectProperty<Languages> languageProperty() {
        return language;
    }

}