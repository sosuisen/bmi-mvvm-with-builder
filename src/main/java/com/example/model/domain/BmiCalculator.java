package com.example.model.domain;

public class BmiCalculator {
    /**
     * Calculate BMI value
     * 
     * @param mHeight  Height (Meter)
     * @param kgWeight Weight (Kilogram)
     * @return
     */
    public double calculateBmi(double mHeight, double kgWeight) {
        if (mHeight <= 0 || kgWeight <= 0)
            throw new IllegalArgumentException();

        return kgWeight / (mHeight * mHeight);
    }
}