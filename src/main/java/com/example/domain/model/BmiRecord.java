package com.example.domain.model;

import java.time.LocalDate;

// Cannot use record since TableColumn::setCellValueFactory cannot handle record.
public class BmiRecord {
    private int id;
    private double heightMeter;
    private double weightKg;
    private LocalDate date;

    public static double calcBmi(double heightMeter, double weightKg) {
        if (heightMeter <= 0 || weightKg <= 0)
            throw new IllegalArgumentException();

        return weightKg / (heightMeter * heightMeter);
    }

    public BmiRecord(int id, double heightMeter, double weightKg, LocalDate date) {
        this.id = id;
        this.heightMeter = heightMeter;
        this.weightKg = weightKg;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getHeightMeter() {
        return heightMeter;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getBmi() {
        return calcBmi(heightMeter, weightKg);
    }

    public ObesityCategory getObesity() {
        return ObesityCategory.getCategory(getBmi());
    }
}
