package com.example.presentation.view.common;

import com.example.domain.model.Languages;
import com.example.domain.model.unit.UnitSystem;

import java.util.Locale;

import com.example.domain.exception.RepositoryException;
import com.example.domain.service.ConfigService;
import com.example.presentation.view.alert.AlertDialog;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CommonViewModel {
    private final ObjectProperty<UnitSystem> unitSystem = new SimpleObjectProperty<>();
    private final ObjectProperty<Languages> language = new SimpleObjectProperty<>();

    public CommonViewModel(ConfigService configService) {
        try {
            unitSystem.set(configService.getUnitSystem());
            language.set(configService.getLanguage());
        } catch (RepositoryException e) {
            AlertDialog.showError(e);
        }

        unitSystem.addListener((_, _, newValue) -> {
            try {
                configService.setUnitSystem(newValue);
            } catch (RepositoryException e) {
                AlertDialog.showError(e);
            }
        });

        language.addListener((_, _, newValue) -> {
            try {
                configService.setLanguage(newValue);
                I18n.getInstance().setResources(Locale.of(newValue.toLanguageString()));
            } catch (RepositoryException e) {
                AlertDialog.showError(e);
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