package com.example.domain.model;

import java.time.LocalDate;

/**
 * Represents the core properties and calculations related to Body Mass Index
 * (BMI).
 * This interface defines methods for calculating BMI and provides access to
 * height, weight, date, and obesity category.
 */
public interface Bmi {
    /**
     * Calculates the Body Mass Index (BMI) given height and weight.
     *
     * @param heightMeter The height in meters. Must be greater than 0.
     * @param weightKg    The weight in kilograms. Must be greater than 0.
     * @return The calculated BMI value.
     * @throws IllegalArgumentException If heightMeter or weightKg is less than or
     *                                  equal to 0.
     */
    static double calcBmi(double heightMeter, double weightKg) {
        if (heightMeter <= 0 || weightKg <= 0)
            throw new IllegalArgumentException();

        return weightKg / (heightMeter * heightMeter);
    }

    /**
     * Returns the unique identifier of the BMI record.
     * 
     * @return The ID.
     */
    int id();

    /**
     * Returns the date associated with the BMI record.
     * 
     * @return The date.
     */
    LocalDate date();

    /**
     * Returns the height in meters.
     * 
     * @return The height.
     */
    double heightMeter();

    /**
     * Returns the weight in kilograms.
     * 
     * @return The weight.
     */
    double weightKg();

    /**
     * Returns the calculated BMI value.
     *
     * @return The BMI.
     */
    double bmi();

    /**
     * Returns the obesity category based on the BMI.
     * 
     * @return The {@link ObesityCategory}.
     */
    ObesityCategory obesity();

}
