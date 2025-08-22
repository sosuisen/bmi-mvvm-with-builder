package com.example.domain.service;

import java.util.List;
import java.util.Optional;

import com.example.domain.model.BmiRecord;
import com.example.domain.exception.RepositoryException;

public interface BmiService {
    public Optional<Double> calculateBmi(double heightMeter, double weightKg);

    public void removeAllRecords() throws RepositoryException;

    public List<BmiRecord> loadBmiRecords() throws RepositoryException;

    public BmiRecord saveBmi(double heightMeter, double weightKg) throws RepositoryException;

}
