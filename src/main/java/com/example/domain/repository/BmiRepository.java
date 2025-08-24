package com.example.domain.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.domain.model.BmiRecord;
import com.example.domain.model.BmiRecordOrder;
import com.example.domain.exception.RepositoryException;

public interface BmiRepository {
    void removeRecord(int id) throws RepositoryException;

    void removeAllRecords() throws RepositoryException;

    void upsertBmiRecord(double height_meter, double weight_kg, LocalDate localDate)
            throws RepositoryException;

    /**
     * Load bmi records by default order(DATE_DESC).
     */
    List<BmiRecord> loadBmiRecords() throws RepositoryException;

    List<BmiRecord> loadBmiRecords(BmiRecordOrder order) throws RepositoryException;
}
