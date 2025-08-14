package com.example.model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.List;

import static jooq.Tables.*;

import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.example.model.domain.BmiRecord;

import jooq.tables.records.BmiHistoryRecord;

public class BmiRepositoryJooqImpl implements BmiRepository {
    private final String DB_PATH = "jdbc:sqlite:./bmi.db";

    private final RecordMapper<BmiHistoryRecord, BmiRecord> mapper;

    public BmiRepositoryJooqImpl() {
        mapper = new BmiRecordMapper();
    }

    @Override
    public BmiRecord saveBmiRecord(BmiRecord bmiRecord) throws RepositoryException {
        var bmiValue = bmiRecord.bmi();
        if (bmiValue <= 0) {
            throw new IllegalArgumentException();
        }

        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            return context.insertInto(BMI_HISTORY)
                    .set(BMI_HISTORY.BMI, bmiValue)
                    .set(BMI_HISTORY.DATETIME, LocalDateTime.now())
                    .returning()
                    .fetchOne(mapper);

        } catch (Exception e) {
            throw new RepositoryException("Failed to save records.");
        }
    }

    @Override
    public List<BmiRecord> loadBmiRecords() throws RepositoryException {
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            return context.selectFrom(BMI_HISTORY)
                    .orderBy(BMI_HISTORY.ID.desc())
                    .fetchInto(BmiRecord.class);
        } catch (Exception e) {
            throw new RepositoryException("Failed to load records.");
        }
    }
}

class BmiRecordMapper implements RecordMapper<BmiHistoryRecord, BmiRecord> {

    @Override
    public BmiRecord map(BmiHistoryRecord record) {
        if (record == null) {
            return null;
        }

        return new BmiRecord(
                record.get(BMI_HISTORY.ID),
                record.get(BMI_HISTORY.BMI),
                record.get(BMI_HISTORY.DATETIME));
    }
}
