package com.example.domain.model.unit;

public final class SIUnitsWithCentimeters implements UnitSystem {

    public static final double M_TO_CM_FACTOR = 100.0;

    @Override
    public double convertHeightToSI(double cm) {
        return cm / M_TO_CM_FACTOR;
    }

    @Override
    public double convertWeightToSI(double kg) {
        return kg;
    }

    @Override
    public double convertHeightFromSI(double meter) {
        return meter * M_TO_CM_FACTOR;
    }

    @Override
    public double convertWeightFromSI(double kg) {
        return kg;
    }

    @Override
    public String getHeightUnit() {
        return "cm";
    }

    @Override
    public String getWeightUnit() {
        return "kg";
    }
}
