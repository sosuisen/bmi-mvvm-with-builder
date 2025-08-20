package com.example.domain.model;

import java.time.LocalDateTime;

public record BmiRecord(int id, double bmi, LocalDateTime datetime) {
    public Obesity.Category obesity() {
        return Obesity.getCategory(bmi);
    }
}
