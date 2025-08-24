package com.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.example.repository.jooq.Tables.*;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.example.domain.model.BmiRecord;
import com.example.domain.repository.BmiRecordOrder;
import com.example.domain.repository.BmiRepository;
import com.example.domain.exception.RepositoryException;

public class BmiRepositoryJooqImpl implements BmiRepository {
    private final String DB_PATH = "jdbc:sqlite:./bmi.db";

    @Override
    public void removeRecord(int id) throws RepositoryException {
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            context.deleteFrom(BMI_HISTORY)
                    .where(BMI_HISTORY.ID.eq(id))
                    .execute();
        } catch (Exception e) {
            throw new RepositoryException("Failed to remove a record.");
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
    public void upsertBmiRecord(double heightMeter, double weightKg, LocalDate localDate)
            throws RepositoryException, NullPointerException, IllegalArgumentException {
        if (heightMeter <= 0 || weightKg <= 0) {
            throw new IllegalArgumentException();
        }
        Objects.requireNonNull(localDate);

        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            context.insertInto(BMI_HISTORY)
                    .set(BMI_HISTORY.HEIGHT_METER, heightMeter)
                    .set(BMI_HISTORY.WEIGHT_KG, weightKg)
                    .set(BMI_HISTORY.DATE, localDate)
                    .onDuplicateKeyUpdate()
                    .set(BMI_HISTORY.HEIGHT_METER, heightMeter)
                    .set(BMI_HISTORY.WEIGHT_KG, weightKg)
                    .execute();
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
}
