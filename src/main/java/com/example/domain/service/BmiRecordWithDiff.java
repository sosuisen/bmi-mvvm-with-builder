package com.example.domain.service;

import java.time.LocalDate;
import java.util.Objects;

import com.example.domain.model.Bmi;
import com.example.domain.model.BmiRecord;
import com.example.domain.model.ObesityCategory;

public record BmiRecordWithDiff(BmiRecord record, BmiRecord prevRecord) implements Bmi {
    public enum Trend {
        SIGNIFICANT_INCREASE, SLIGHT_INCREASE, STABLE, SLIGHT_DECREASE, SIGNIFICANT_DECREASE, NONE;

        public String toResourceString() {
            return toString().toLowerCase();
        }
    }

    public BmiRecordWithDiff {
        Objects.requireNonNull(record, "record must not be null");
    }

    public boolean hasPrevRecord() {
        return prevRecord != null;
    }

    /**
     * Return the difference between current and previous BMI values;
     * return 0 if prevRecord is null.
     */
    public double diff() {
        if (!hasPrevRecord()) {
            return 0;
        }
        return record.bmi() - prevRecord.bmi();
    }

    /**
     * Get a trend description. Returns NONE if prevRecord is null.
     */
    public Trend trendDescription() {
        if (prevRecord == null)
            return Trend.NONE;
        double diff = diff();
        if (diff > 0.5)
            return Trend.SIGNIFICANT_INCREASE;
        if (diff > 0.1)
            return Trend.SLIGHT_INCREASE;
        if (diff < -0.5)
            return Trend.SIGNIFICANT_DECREASE;
        if (diff < -0.1)
            return Trend.SLIGHT_DECREASE;

        return Trend.STABLE;
    }

    @Override
    public int id() {
        return record.id();
    }

    @Override
    public double heightMeter() {
        return record.heightMeter();
    }

    @Override
    public double weightKg() {
        return record.weightKg();
    }

    @Override
    public LocalDate date() {
        return record.date();
    }

    @Override
    public double bmi() {
        return record.bmi();
    }

    @Override
    public ObesityCategory obesity() {
        return record.obesity();
    }
}
