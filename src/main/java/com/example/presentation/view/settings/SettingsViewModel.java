package com.example.presentation.view.settings;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.Languages;
import com.example.domain.model.unit.UnitSystem;
import com.example.presentation.view.application.BmiCommonAppModel;
import com.example.presentation.view.application.ConfigAppModel;

import javafx.beans.property.ObjectProperty;

public class SettingsViewModel {
    private final ConfigAppModel commonViewModel;
    private final BmiCommonAppModel bmiListAppModel;

    protected ObjectProperty<UnitSystem> unitSystemProperty() {
        return commonViewModel.unitSystemProperty();
    }

    protected ObjectProperty<Languages> languageProperty() {
        return commonViewModel.languageProperty();
    }

    public SettingsViewModel(BmiCommonAppModel bmiListAppModel, ConfigAppModel commonViewModel) {
        this.commonViewModel = commonViewModel;
        this.bmiListAppModel = bmiListAppModel;
    }

    protected void removeAllRecords() throws RepositoryException {
        bmiListAppModel.removeAllRecords();
    }

}
