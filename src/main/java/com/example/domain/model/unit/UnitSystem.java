package com.example.domain.model.unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a system of units for height and weight measurements. This sealed interface ensures
 * that only a predefined set of unit systems (e.g., SI, Imperial) can implement it.
 */
public sealed interface UnitSystem permits SIUnitsWithCentimeters, ImperialUnits {

    /**
     * Returns a list of all available {@link UnitSystem} implementations. This method dynamically
     * instantiates each permitted subclass of {@code UnitSystem}.
     *
     * @return A list of all concrete {@link UnitSystem} instances.
     * @throws RuntimeException if any of the permitted subclasses cannot be instantiated (e.g.,
     *         missing no-argument constructor, or other reflection issues).
     */
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

    /**
     * Converts a height value from this unit system to the SI unit (meters).
     *
     * @param value The height value in this unit system.
     * @return The converted height value in meters.
     */
    double convertHeightToSI(double value);

    /**
     * Converts a weight value from this unit system to the SI unit (kilograms).
     *
     * @param value The weight value in this unit system.
     * @return The converted weight value in kilograms.
     */
    double convertWeightToSI(double value);

    /**
     * Converts a height value from SI unit (meters) to this unit system.
     *
     * @param value The height value in meters.
     * @return The converted height value in this unit system.
     */
    double convertHeightFromSI(double value);

    /**
     * Converts a weight value from SI unit (kilograms) to this unit system.
     *
     * @param value The weight value in kilograms.
     * @return The converted weight value in this unit system.
     */
    double convertWeightFromSI(double value);

    /**
     * Returns the string representation of the height unit for this system. For example, "cm" for
     * SI or "ft" for Imperial.
     *
     * @return The height unit string.
     */
    String getHeightUnit();

    /**
     * Returns the string representation of the weight unit for this system. For example, "kg" for
     * SI or "lb" for Imperial.
     *
     * @return The weight unit string.
     */
    String getWeightUnit();

    /**
     * Returns a lowercase string representation of the unit system's simple class name, suitable
     * for use as a resource key.
     *
     * @return The resource key string (e.g., "siunitswithcentimeters", "imperialunits").
     */
    default String toResourceString() {
        return this.getClass().getSimpleName().toLowerCase();
    }

}
