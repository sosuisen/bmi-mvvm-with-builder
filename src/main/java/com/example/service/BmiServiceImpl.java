package com.example.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.domain.model.BmiRecord;
import com.example.domain.repository.BmiRecordOrder;
import com.example.domain.repository.BmiRepository;
import com.example.domain.exception.RepositoryException;
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
            return Optional.of(BmiRecord.calcBmi(heightMeter, weightKg));
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
    public List<BmiRecordWithDiff> loadBmiRecords() throws RepositoryException {
        var recordsWithDiff = new ArrayList<BmiRecordWithDiff>();
        BmiRecord prevRecord = null;
        for (var record : repository.loadBmiRecords(BmiRecordOrder.DATE_ASC)) {
            recordsWithDiff.addFirst(new BmiRecordWithDiff(record, prevRecord));
            prevRecord = record;
        }
        return recordsWithDiff;
    }

    @Override
    public BmiRecordWithDiff saveBmi(double heightMeter, double weightKg) throws RepositoryException {
        return saveBmi(heightMeter, weightKg, LocalDate.now());
    }

    @Override
    public BmiRecordWithDiff saveBmi(double heightMeter, double weightKg, LocalDate localDate)
            throws RepositoryException {
        var record = repository.saveBmiRecord(heightMeter, weightKg, localDate);
        var prevRecord = repository.findWithOffset(BmiRecordOrder.DATE_DESC, 1);
        return new BmiRecordWithDiff(record, prevRecord);
    }
}
