package com.example.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.domain.model.BmiRecord;
import com.example.domain.repository.BmiRepository;
import com.example.domain.exception.RepositoryException;
import com.example.domain.service.BmiService;

public class BmiServiceImpl implements BmiService {
    private final BmiRepository repository;

    public BmiServiceImpl(BmiRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Double> calculateBmi(double heightMeter, double weightKg) {
        try {
            return Optional.of(BmiRecord.calcBmi(heightMeter, weightKg));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public void removeAllRecords() throws RepositoryException {
        repository.removeAllRecords();
    }

    @Override
    public List<BmiRecord> loadBmiRecords() throws RepositoryException {
        return repository.loadBmiRecords();
    }

    @Override
    public BmiRecord saveBmi(double heightMeter, double weightKg) throws RepositoryException {
        return repository.saveBmiRecord(heightMeter, weightKg, LocalDate.now());
    }
}
