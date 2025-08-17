package com.example.model;

import java.util.List;
import java.util.Optional;

import com.example.model.domain.BmiCalculator;
import com.example.model.domain.BmiRecord;
import com.example.model.domain.Unit;
import com.example.model.repository.BmiRepository;
import com.example.model.repository.RepositoryException;

public class BmiService {
    private final BmiRepository repository;
    private final BmiCalculator calculator;
    private final Unit unit;

    public BmiService(BmiRepository repository) {
        this.repository = repository;
        calculator = new BmiCalculator();
        unit = new Unit();
    }

    public Optional<Double> calculateBmi(double mHeight, double kgWeight) {
        try {
            return Optional.of(calculator.calculateBmi(mHeight, kgWeight));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public double convertHeightToSI(Unit.UnitSystem unitSystem, double value) {
        return unit.convertHeightToSI(unitSystem, value);
    }

    public double convertWeightToSI(Unit.UnitSystem unitSystem, double value) {
        return unit.convertWeightToSI(unitSystem, value);
    }

    public double convertHeightFromSI(Unit.UnitSystem unitSystem, double value) {
        return unit.convertHeightFromSI(unitSystem, value);
    }

    public double convertWeightFromSI(Unit.UnitSystem unitSystem, double value) {
        return unit.convertWeightFromSI(unitSystem, value);
    }

    public List<BmiRecord> loadBmiRecords() throws RepositoryException {
        return repository.loadBmiRecords();
    }

    public BmiRecord saveBmiRecord(BmiRecord recordToSave) throws RepositoryException {
        var newRecord = repository.saveBmiRecord(recordToSave);
        return newRecord;
    }

}
