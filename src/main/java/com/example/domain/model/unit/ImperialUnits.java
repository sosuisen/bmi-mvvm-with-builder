package com.example.domain.model.unit;

public final class ImperialUnits implements UnitSystem {

    public static final double M_TO_FT_FACTOR = 3.2808;
    public static final double KG_TO_LB_FACTOR = 2.2046;

    public double convertHeightToSI(double value) {
        return value / M_TO_FT_FACTOR;
    }

    public double convertWeightToSI(double value) {
        return value / KG_TO_LB_FACTOR;
    }

    public double convertHeightFromSI(double value) {
        return value * M_TO_FT_FACTOR;
    }

    public double convertWeightFromSI(double value) {
        return value * KG_TO_LB_FACTOR;
    }

    public String getHeightUnit() {
        return "ft";
    }

    public String getWeightUnit() {
        return "lb";
    }
}
