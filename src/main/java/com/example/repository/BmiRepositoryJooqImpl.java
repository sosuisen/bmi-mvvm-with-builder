package com.example.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.example.repository.jooq.Tables.*;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.BmiRecord;
import com.example.domain.model.BmiRecordOrder;
import com.example.domain.repository.BmiRepository;

public class BmiRepositoryJooqImpl implements BmiRepository {
    private final String JDBC_URL_PREFIX = "jdbc:sqlite:";
    private final String DB_NAME = "bmi.db";
    private final String DB_PATH;

    public BmiRepositoryJooqImpl() throws RepositoryException {
        DB_PATH = JDBC_URL_PREFIX + AppDirectory.getAppDirPath() + "/" + DB_NAME;

        try {
            Files.createDirectories(Path.of(AppDirectory.getAppDirPath()));
        } catch (IOException e) {
            throw new RepositoryException("Failed to create app directory.");
        }

        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws RepositoryException {
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            context.execute("""
                            CREATE TABLE IF NOT EXISTS bmi_history (
                                id INTEGER NOT NULL,
                                height_meter REAL NOT NULL,
                                weight_kg REAL NOT NULL,
                                date TEXT NOT NULL UNIQUE,
                                CONSTRAINT bmi_pk PRIMARY KEY (id)
                            )
                            """);
        } catch (Exception e) {
            throw new RepositoryException("Failed to create a table.");
        }
    }

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
        if (heightMeter <= 0 || weightKg <= 0) { throw new IllegalArgumentException(); }
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
    public List<BmiRecord> loadBmiRecords(BmiRecordOrder order) throws RepositoryException {
        return loadBmiRecordsInternal(order, null);
    }

    @Override
    public List<BmiRecord> loadBmiRecords(BmiRecordOrder order, int limit)
        throws RepositoryException {
        return loadBmiRecordsInternal(order, limit);
    }

    /**
     * Loads BMI records from the database.
     * 
     * @param order The order in which to sort the records (ascending or descending by date).
     * @param limit The maximum number of records to retrieve, or null for no limit.
     * @return A list of BmiRecord objects.
     * @throws RepositoryException If there is an error loading the records.
     */
    private List<BmiRecord> loadBmiRecordsInternal(BmiRecordOrder order, Integer limit)
        throws RepositoryException {
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            var context = DSL.using(conn, SQLDialect.SQLITE);
            return context.selectFrom(BMI_HISTORY)
                .orderBy(
                    switch (order) {
                        case DATE_ASC -> BMI_HISTORY.DATE.asc();
                        case DATE_DESC -> BMI_HISTORY.DATE.desc();
                    }
                )
                .limit(limit)
                .fetchInto(BmiRecord.class);
        } catch (Exception e) {
            throw new RepositoryException("Failed to load records.", e);
        }
    }
}
