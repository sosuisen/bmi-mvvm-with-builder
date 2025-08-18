package com.example.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.model.domain.BmiRecord;

public interface BmiRepository {
    BmiRecord saveBmiRecord(double bmi, LocalDateTime localDateTime) throws RepositoryException;

    List<BmiRecord> loadBmiRecords() throws RepositoryException;
}
