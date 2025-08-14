package com.example.ui.main;

import com.example.model.BmiService;
import com.example.model.domain.BmiRecord;
import com.example.model.repository.RepositoryException;
import com.example.ui.AlertDialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainViewModel {
    private final BmiService service;

    private final ObservableList<BmiRecord> bmiList = FXCollections.observableArrayList();

    public ObservableList<BmiRecord> getBmiList() {
        return bmiList;
    }

    public MainViewModel(BmiService service) {
        this.service = service;
        try {
            bmiList.setAll(service.loadBmiRecords());
        } catch (RepositoryException e) {
            AlertDialog.showError(e);
        }
    }
}
