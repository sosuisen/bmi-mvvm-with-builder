package com.example.domain.model;

import java.time.LocalDate;

/**
 * Represents a single BMI record, including height, weight, date, and calculated BMI and obesity
 * category.
 *
 * @param id The unique identifier of the BMI record.
 * @param heightMeter The height in meters.
 * @param weightKg The weight in kilograms.
 * @param date The date of the BMI record. This field is used as a unique key for upsert operations
 *        and for ordering records.
 */
public record BmiRecord(
    int id,
    double heightMeter,
    double weightKg,
    LocalDate date) implements Bmi {

    @Override
    public double bmi() {
        return Bmi.calcBmi(heightMeter(), weightKg());
    }

    @Override
    public ObesityCategory obesity() {
        return ObesityCategory.getCategory(bmi());
    }
}
