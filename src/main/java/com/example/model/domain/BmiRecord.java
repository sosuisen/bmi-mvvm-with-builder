package com.example.model.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public record BmiRecord(int id, double bmi, LocalDateTime datetime) {
    public static final int UNASSIGNED_ID = -1;

    public Obesity.Category obesity() {
        return Obesity.getCategory(bmi);
    }

    public BmiRecord {
        if (id <= 0 && id != UNASSIGNED_ID) {
            throw new IllegalArgumentException("id must be > 0, got: " + id);
        }
        if (bmi <= 0) {
            throw new IllegalArgumentException("bmi must be positive, got: " + bmi);
        }
        Objects.requireNonNull(datetime, "datetime cannot be null");
    }

    public static BmiRecord withoutId(double bmi, LocalDateTime datetime) {
        return new BmiRecord(UNASSIGNED_ID, bmi, datetime);
    }
}
