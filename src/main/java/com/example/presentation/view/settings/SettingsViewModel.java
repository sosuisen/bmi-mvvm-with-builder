package com.example.presentation.view.settings;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.Languages;
import com.example.domain.model.unit.UnitSystem;
import com.example.domain.service.BmiService;
import com.example.presentation.view.common.CommonViewModel;

import javafx.beans.property.ObjectProperty;

public class SettingsViewModel {
    private final CommonViewModel commonViewModel;
    private final BmiService bmiService;

    public ObjectProperty<UnitSystem> unitSystemProperty() {
        return commonViewModel.unitSystemProperty();
    }

    public ObjectProperty<Languages> languageProperty() {
        return commonViewModel.languageProperty();
    }

    public SettingsViewModel(BmiService bmiService, CommonViewModel commonViewModel) {
        this.bmiService = bmiService;
        this.commonViewModel = commonViewModel;
    }

    public void removeAllRecords() throws RepositoryException {
        commonViewModel.getBmiList().clear();
        bmiService.removeAllRecords();
    }

}
