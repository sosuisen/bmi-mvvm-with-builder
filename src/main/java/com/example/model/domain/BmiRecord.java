package com.example.model.domain;

import java.time.LocalDateTime;

public record BmiRecord(int id, double bmi, LocalDateTime datetime) {
    public ObesityCategory obesity() {
        if (bmi <= 0) {
            return ObesityCategory.NONE;
        } else if (bmi < 18.5) {
            return ObesityCategory.LOW;
        } else if (bmi < 25.0) {
            return ObesityCategory.NORMAL;
        } else {
            return ObesityCategory.HIGH;
        }
    }
}
