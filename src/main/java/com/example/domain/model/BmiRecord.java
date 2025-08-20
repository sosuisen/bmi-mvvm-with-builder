package com.example.domain.model;

import java.time.LocalDateTime;

public record BmiRecord(int id, double bmi, LocalDateTime datetime) {
    public ObesityCategory obesity() {
        return ObesityCategory.getCategory(bmi);
    }
}
