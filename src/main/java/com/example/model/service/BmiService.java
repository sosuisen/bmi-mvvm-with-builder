package com.example.model.service;

import java.util.List;
import java.util.Optional;

import com.example.model.domain.BmiRecord;
import com.example.model.repository.RepositoryException;

public interface BmiService {
    public Optional<Double> calculateBmi(double mHeight, double kgWeight);

    public List<BmiRecord> loadBmiRecords() throws RepositoryException;

    public BmiRecord saveBmi(double bmi) throws RepositoryException;

}
