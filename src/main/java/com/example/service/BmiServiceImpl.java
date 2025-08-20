package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.domain.model.BmiCalculator;
import com.example.domain.model.BmiRecord;
import com.example.domain.repository.BmiRepository;
import com.example.domain.exception.RepositoryException;
import com.example.domain.service.BmiService;

public class BmiServiceImpl implements BmiService {
    private final BmiRepository repository;
    private final BmiCalculator calculator;

    public BmiServiceImpl(BmiRepository repository) {
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
