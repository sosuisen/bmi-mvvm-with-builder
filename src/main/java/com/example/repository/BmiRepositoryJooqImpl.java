package com.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.example.repository.jooq.Tables.*;

import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.example.domain.model.BmiRecord;
import com.example.domain.repository.BmiRecordOrder;
import com.example.domain.repository.BmiRepository;
import com.example.domain.exception.RepositoryException;

import com.example.repository.jooq.tables.records.BmiHistoryRecord;

public class BmiRepositoryJooqImpl implements BmiRepository {
    private final String DB_PATH = "jdbc:sqlite:./bmi.db";

    private final RecordMapper<BmiHistoryRecord, BmiRecord> mapper;

    public BmiRepositoryJooqImpl() {
        mapper = new BmiRecordMapper();
    }

    @Override
    public void removeRecord(int id) throws RepositoryException {
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            context.deleteFrom(BMI_HISTORY)
                    .where(BMI_HISTORY.ID.eq(id))
                    .execute();
        } catch (Exception e) {
            throw new RepositoryException("Failed to remove all records.");
        }

    }

    @Override
    public void removeAllRecords() throws RepositoryException {
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            context.delete(BMI_HISTORY)
                    .execute();
        } catch (Exception e) {
            throw new RepositoryException("Failed to remove all records.");
        }
    }

    @Override
    public BmiRecord saveBmiRecord(double heightMeter, double weightKg, LocalDate localDate)
            throws RepositoryException, NullPointerException, IllegalArgumentException {
        if (heightMeter <= 0 || weightKg <= 0) {
            throw new IllegalArgumentException();
        }
        Objects.requireNonNull(localDate);

        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            return context.insertInto(BMI_HISTORY)
                    .set(BMI_HISTORY.HEIGHT_METER, heightMeter)
                    .set(BMI_HISTORY.WEIGHT_KG, weightKg)
                    .set(BMI_HISTORY.DATE, localDate)
                    .returning()
                    .fetchOne(mapper);

        } catch (Exception e) {
            throw new RepositoryException("Failed to save records.");
        }
    }

    @Override
    public List<BmiRecord> loadBmiRecords() throws RepositoryException {
        return loadBmiRecords(BmiRecordOrder.DATE_DESC);
    }

    @Override
    public List<BmiRecord> loadBmiRecords(BmiRecordOrder order) throws RepositoryException {
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            return context.selectFrom(BMI_HISTORY)
                    .orderBy(
                            switch (order) {
                                case DATE_ASC -> BMI_HISTORY.DATE.asc();
                                case DATE_DESC -> BMI_HISTORY.DATE.desc();
                            })
                    .fetchInto(BmiRecord.class);
        } catch (Exception e) {
            throw new RepositoryException("Failed to load records.");
        }
    }

    @Override
    public BmiRecord findWithOffset(BmiRecordOrder order, int offset) throws RepositoryException {
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            return context.selectFrom(BMI_HISTORY)
                    .orderBy(
                            switch (order) {
                                case DATE_ASC -> BMI_HISTORY.DATE.asc();
                                case DATE_DESC -> BMI_HISTORY.DATE.desc();
                            })
                    .limit(1)
                    .offset(offset)
                    .fetchOne(mapper);
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
                record.get(BMI_HISTORY.HEIGHT_METER),
                record.get(BMI_HISTORY.WEIGHT_KG),
                record.get(BMI_HISTORY.DATE));
    }
}
