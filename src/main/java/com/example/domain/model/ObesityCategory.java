package com.example.domain.model;

public enum ObesityCategory {

    NONE, LOW, NORMAL, HIGH;

    public String toResourceString() {
        return toString().toLowerCase();
    }

    public static ObesityCategory getCategory(double bmi) {
        if (bmi <= 0) {
            return NONE;
        } else if (bmi < 18.5) {
            return LOW;
        } else if (bmi < 25.0) {
            return NORMAL;
        } else {
            return HIGH;
        }
    }
}
