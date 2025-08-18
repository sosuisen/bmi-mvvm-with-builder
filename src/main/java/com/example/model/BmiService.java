package com.example.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.model.domain.BmiCalculator;
import com.example.model.domain.BmiRecord;
import com.example.model.repository.BmiRepository;
import com.example.model.repository.RepositoryException;

public class BmiService {
    private final BmiRepository repository;
    private final BmiCalculator calculator;

    public BmiService(BmiRepository repository) {
        this.repository = repository;
        calculator = new BmiCalculator();
    }

    public Optional<Double> calculateBmi(double mHeight, double kgWeight) {
        try {
            return Optional.of(calculator.calculateBmi(mHeight, kgWeight));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public List<BmiRecord> loadBmiRecords() throws RepositoryException {
        return repository.loadBmiRecords();
    }

    public BmiRecord saveBmi(double bmi) throws RepositoryException {
        return repository.saveBmiRecord(bmi, LocalDateTime.now());
    }

}
