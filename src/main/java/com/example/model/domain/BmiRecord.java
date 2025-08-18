package com.example.model.domain;

import java.time.LocalDateTime;

public record BmiRecord(int id, double bmi, LocalDateTime datetime) {
    public Obesity.Category obesity() {
        return Obesity.getCategory(bmi);
    }
}
