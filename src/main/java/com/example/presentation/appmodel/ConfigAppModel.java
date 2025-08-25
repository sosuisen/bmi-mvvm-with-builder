package com.example.presentation.view.application;

import com.example.domain.model.Languages;
import com.example.domain.model.unit.SIUnitsWithCentimeters;
import com.example.domain.model.unit.UnitSystem;

import java.util.Locale;

import com.example.domain.exception.RepositoryException;
import com.example.domain.service.ConfigService;
import com.example.presentation.utils.I18n;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ConfigAppModel {
    private final ObjectProperty<UnitSystem> unitSystem = new SimpleObjectProperty<>();
    private final ObjectProperty<Languages> language = new SimpleObjectProperty<>();

    public ConfigAppModel(ConfigService configService, BmiCommonAppModel bmiListAppModel) {
        try {
            unitSystem.set(configService.getUnitSystem());
        } catch (RepositoryException e) {
            unitSystem.set(new SIUnitsWithCentimeters());
            bmiListAppModel.errorProperty().set(e);
        }

        try {
            language.set(configService.getLanguage());
        } catch (RepositoryException e) {
            language.set(Languages.getDefaultLanguages());
            bmiListAppModel.errorProperty().set(e);
        }

        unitSystem.addListener((_, _, newValue) -> {
            try {
                configService.setUnitSystem(newValue);
                // Reload to call a cellfactory to display converted values
                // based on the new unit system.
                bmiListAppModel.reloadRecords();
            } catch (RepositoryException e) {
                bmiListAppModel.errorProperty().set(e);
            }
        });

        language.addListener((_, _, newValue) -> {
            try {
                configService.setLanguage(newValue);
                I18n.getInstance().setResources(Locale.of(newValue.toLanguageString()));
                // Reload to call a cellfactory to display converted values
                // based on the new language.
                bmiListAppModel.reloadRecords();

            } catch (RepositoryException e) {
                bmiListAppModel.errorProperty().set(e);
            }
        });
    }

    public ObjectProperty<UnitSystem> unitSystemProperty() {
        return unitSystem;
    }

    public ObjectProperty<Languages> languageProperty() {
        return language;
    }

}