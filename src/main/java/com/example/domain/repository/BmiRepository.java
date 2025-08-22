package com.example.domain.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.domain.model.BmiRecord;
import com.example.domain.exception.RepositoryException;

public interface BmiRepository {
    void removeAllRecords() throws RepositoryException;

    BmiRecord saveBmiRecord(double height_meter, double weight_kg, LocalDate localDate)
            throws RepositoryException;

    List<BmiRecord> loadBmiRecords() throws RepositoryException;
}
