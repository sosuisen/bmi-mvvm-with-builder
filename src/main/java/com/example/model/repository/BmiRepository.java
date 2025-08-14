package com.example.model.repository;

import java.util.List;

import com.example.model.domain.BmiRecord;

public interface BmiRepository {
    BmiRecord saveBmiRecord(BmiRecord bmiRecord) throws RepositoryException;

    List<BmiRecord> loadBmiRecords() throws RepositoryException;
}
