package com.example.model.domain;

public class Unit {
    public enum UnitSystem {
        SI, Imperial
    }

    public static final double M_TO_FT_FACTOR = 3.2808;
    public static final double M_TO_CM_FACTOR = 100;
    public static final double KG_TO_LB_FACTOR = 2.2046;

    public double convertHeightToSI(UnitSystem unitSystem, double value) {
        return switch (unitSystem) {
            case UnitSystem.Imperial -> value / M_TO_FT_FACTOR;
            case UnitSystem.SI -> value / M_TO_CM_FACTOR;
        };
    }

    public double convertWeightToSI(UnitSystem unitSystem, double value) {
        return switch (unitSystem) {
            case UnitSystem.Imperial -> value / KG_TO_LB_FACTOR;
            case UnitSystem.SI -> value;
        };
    }

    public double convertHeightFromSI(UnitSystem unitSystem, double value) {
        return switch (unitSystem) {
            case UnitSystem.Imperial -> value * M_TO_FT_FACTOR;
            case UnitSystem.SI -> value * M_TO_CM_FACTOR;
        };
    }

    public double convertWeightFromSI(UnitSystem unitSystem, double value) {
        return switch (unitSystem) {
            case UnitSystem.Imperial -> value * KG_TO_LB_FACTOR;
            case UnitSystem.SI -> value;
        };
    }
}
