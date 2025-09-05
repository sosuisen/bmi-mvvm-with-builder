package com.example.domain.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.BmiRecord;
import com.example.domain.model.BmiRecordOrder;

public interface BmiRepository {
    /**
     * Removes a BMI record from the repository by its ID.
     *
     * @param id The ID of the BMI record to remove.
     * @throws RepositoryException If an error occurs during the removal process.
     */
    void removeRecord(int id) throws RepositoryException;

    /**
     * Removes all BMI records from the repository.
     *
     * @throws RepositoryException If an error occurs during the removal process.
     */
    void removeAllRecords() throws RepositoryException;

    /**
     * Inserts or updates a BMI record in the repository. If a record with the same date already
     * exists, it will be updated.
     *
     * @param height_meter The height in meters.
     * @param weight_kg The weight in kilograms.
     * @param localDate The date of the BMI record.
     * @throws RepositoryException If an error occurs during the upsert process.
     */
    void upsertBmiRecord(double height_meter, double weight_kg, LocalDate localDate)
        throws RepositoryException;

    /**
     * Loads a limited number of BMI records from the repository, ordered by date.
     *
     * @param order The order in which to sort the records (ascending or descending by date).
     * @param limit The maximum number of records to retrieve.
     * @return A list of {@link BmiRecord} objects.
     * @throws RepositoryException If an error occurs during the loading process.
     */
    List<BmiRecord> loadBmiRecords(BmiRecordOrder order, int limit) throws RepositoryException;

    /**
     * Loads all BMI records from the repository, ordered by date.
     *
     * @param order The order in which to sort the records (ascending or descending by date).
     * @return A list of {@link BmiRecord} objects.
     * @throws RepositoryException If an error occurs during the loading process.
     */
    List<BmiRecord> loadBmiRecords(BmiRecordOrder order) throws RepositoryException;
}
