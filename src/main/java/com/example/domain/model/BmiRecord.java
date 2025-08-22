package com.example.domain.model;

import java.time.LocalDate;

public record BmiRecord(
        int id,
        double heightMeter,
        double weightKg,
        LocalDate date) {

    public static double calcBmi(double heightMeter, double weightKg) {
        if (heightMeter <= 0 || weightKg <= 0)
            throw new IllegalArgumentException();

        return weightKg / (heightMeter * heightMeter);
    }

    public double bmi() {
        return calcBmi(heightMeter, weightKg);
    }

    public ObesityCategory obesity() {
        return ObesityCategory.getCategory(bmi());
    }
}
