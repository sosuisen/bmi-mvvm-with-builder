package com.example.domain.model;

import java.time.LocalDate;

public record BmiRecord(
        int id,
        double heightMeter,
        double weightKg,
        LocalDate date) implements Bmi {

    public static double calcBmi(double heightMeter, double weightKg) {
        if (heightMeter <= 0 || weightKg <= 0)
            throw new IllegalArgumentException();

        return weightKg / (heightMeter * heightMeter);
    }

    @Override
    public double bmi() {
        return calcBmi(heightMeter, weightKg);
    }

    @Override
    public ObesityCategory obesity() {
        return ObesityCategory.getCategory(bmi());
    }
}
