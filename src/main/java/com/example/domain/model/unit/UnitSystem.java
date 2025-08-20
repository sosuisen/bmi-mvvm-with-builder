package com.example.domain.model.unit;

import java.util.ArrayList;
import java.util.List;

public sealed interface UnitSystem permits SIUnitsWithCentimeters, ImperialUnits {
    static List<UnitSystem> getAll() {
        var unitSystems = UnitSystem.class.getPermittedSubclasses();
        List<UnitSystem> list = new ArrayList<>();
        for (var unitSystem : unitSystems) {
            try {
                list.add((UnitSystem) unitSystem.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    double convertHeightToSI(double value);

    double convertWeightToSI(double value);

    double convertHeightFromSI(double value);

    double convertWeightFromSI(double value);
}
