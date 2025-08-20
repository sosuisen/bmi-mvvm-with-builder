package com.example.domain.model.unit;

public sealed interface Units permits SIUnitsWithCentimeters, ImperialUnits {
    double convertHeightToSI(double value);

    double convertWeightToSI(double value);

    double convertHeightFromSI(double value);

    double convertWeightFromSI(double value);
}
