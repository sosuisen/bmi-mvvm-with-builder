package com.example.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.domain.exception.RepositoryException;

public interface BmiService {
    /**
     * Calculates the Body Mass Index (BMI) based on height and weight.
     *
     * @param heightMeter The height in meters.
     * @param weightKg The weight in kilograms.
     * @return An {@link Optional} containing the calculated BMI if inputs are valid, otherwise an
     *         empty Optional.
     */
    public Optional<Double> calculateBmi(double heightMeter, double weightKg);

    /**
     * Removes a specific BMI record by its ID.
     *
     * @param id The ID of the BMI record to remove.
     * @throws RepositoryException If an error occurs during the removal process.
     */
    public void removeRecord(int id) throws RepositoryException;

    /**
     * Removes all BMI records from the repository.
     *
     * @throws RepositoryException If an error occurs during the removal process.
     */
    public void removeAllRecords() throws RepositoryException;

    /**
     * Loads a limited number of BMI records from the repository, including differences from the
     * previous record.
     * 
     * The records are loaded in a descending order.
     *
     * @param limit The maximum number of records to retrieve.
     * @return A list of {@link BmiRecordWithDiff} objects.
     * @throws RepositoryException If an error occurs during the loading process.
     */
    public List<BmiRecordWithDiff> loadRecords(int limit) throws RepositoryException;

    /**
     * Inserts or updates a BMI record in the repository.
     *
     * @param heightMeter The height in meters.
     * @param weightKg The weight in kilograms.
     * @param date The date of the BMI record.
     * @throws RepositoryException If an error occurs during the upsert process.
     */
    public void upsertRecord(double heightMeter, double weightKg, LocalDate date)
        throws RepositoryException;
}
