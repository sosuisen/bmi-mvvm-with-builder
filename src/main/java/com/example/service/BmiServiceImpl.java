package com.example.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.Bmi;
import com.example.domain.model.BmiRecord;
import com.example.domain.model.BmiRecordOrder;
import com.example.domain.repository.BmiRepository;
import com.example.domain.service.BmiRecordWithDiff;
import com.example.domain.service.BmiService;

public class BmiServiceImpl implements BmiService {
    private final BmiRepository repository;

    public BmiServiceImpl(BmiRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Double> calculateBmi(double heightMeter, double weightKg) {
        try {
            return Optional.of(Bmi.calcBmi(heightMeter, weightKg));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public void removeRecord(int id) throws RepositoryException {
        repository.removeRecord(id);
    }

    @Override
    public void removeAllRecords() throws RepositoryException {
        repository.removeAllRecords();
    }

    @Override
    public List<BmiRecordWithDiff> loadRecords(int limit) throws RepositoryException {
        var recordsWithDiff = new ArrayList<BmiRecordWithDiff>();
        BmiRecord prevRecord = null;
        for (var record : repository.loadBmiRecords(BmiRecordOrder.DATE_DESC, limit).reversed()) {
            recordsWithDiff.addFirst(new BmiRecordWithDiff(record, prevRecord));
            prevRecord = record;
        }
        return recordsWithDiff;
    }

    @Override
    public void upsertRecord(double heightMeter, double weightKg, LocalDate date)
            throws RepositoryException {
        repository.upsertBmiRecord(heightMeter, weightKg, date);
    }
}