package com.example.model;

import java.util.List;

import com.example.model.domain.BmiRecord;
import com.example.model.repository.BmiRepository;
import com.example.model.repository.BmiRepositoryJooqImpl;
import com.example.model.repository.RepositoryException;

public class BmiService {
    private final BmiRepository repository;

    public BmiService() {
        repository = new BmiRepositoryJooqImpl();
    }

    public double calcBmi(double mHeight, double kgWeight) {
        if (mHeight <= 0 || kgWeight <= 0)
            throw new IllegalArgumentException();

        return kgWeight / (mHeight * mHeight);
    }

    public List<BmiRecord> loadBmiRecords() throws RepositoryException {
        return repository.loadBmiRecords();
    }

    public BmiRecord saveBmiRecord(BmiRecord recordToSave) throws RepositoryException {
        var newRecord = repository.saveBmiRecord(recordToSave);
        return newRecord;
    }

}
