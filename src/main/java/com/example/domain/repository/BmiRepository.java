package com.example.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.domain.model.BmiRecord;
import com.example.domain.exception.RepositoryException;

public interface BmiRepository {
    void removeAllRecords() throws RepositoryException;

    BmiRecord saveBmiRecord(double bmi, LocalDateTime localDateTime) throws RepositoryException;

    List<BmiRecord> loadBmiRecords() throws RepositoryException;
}
