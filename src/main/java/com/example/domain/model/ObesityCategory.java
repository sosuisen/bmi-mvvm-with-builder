package com.example.domain.model;

public enum ObesityCategory {

    NONE, LOW, NORMAL, HIGH;

    /**
     * Converts the enum constant name to a lowercase string, suitable for resource keys.
     *
     * @return The lowercase string representation of the enum constant.
     */
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
