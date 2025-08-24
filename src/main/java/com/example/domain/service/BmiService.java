package com.example.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.domain.exception.RepositoryException;

public interface BmiService {
    public Optional<Double> calculateBmi(double heightMeter, double weightKg);

    public void removeRecord(int id) throws RepositoryException;

    public void removeAllRecords() throws RepositoryException;

    public List<BmiRecordWithDiff> loadBmiRecords() throws RepositoryException;

    public BmiRecordWithDiff saveBmi(double heightMeter, double weightKg) throws RepositoryException;

    public BmiRecordWithDiff saveBmi(double heightMeter, double weightKg, LocalDate date) throws RepositoryException;

}
