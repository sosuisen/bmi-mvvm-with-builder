package com.example.domain.model.unit;

public final class SIUnitsWithCentimeters implements UnitSystem {

    public static final double M_TO_CM_FACTOR = 100.0;

    public double convertHeightToSI(double cm) {
        return cm / M_TO_CM_FACTOR;
    }

    public double convertWeightToSI(double kg) {
        return kg;
    }

    public double convertHeightFromSI(double meter) {
        return meter * M_TO_CM_FACTOR;
    }

    public double convertWeightFromSI(double kg) {
        return kg;
    }

    public String getHeightUnit() {
        return "cm";
    }

    public String getWeightUnit() {
        return "kg";
    }
}
