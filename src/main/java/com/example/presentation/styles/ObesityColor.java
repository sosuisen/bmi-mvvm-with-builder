package com.example.presentation.styles;

import com.example.domain.model.ObesityCategory;

public class ObesityColor {
    /*
     * Get web-style color string. E.g) "#ffff00"
     */
    public static String getLightColor(ObesityCategory category) {
        return switch (category) {
            case NONE -> "#90a0a0";
            case LOW -> "#c0e0ff";
            case NORMAL -> "#d0f0e0";
            case HIGH -> "#ffd0d0";
        };
    }

    public static String getDarkColor(ObesityCategory category) {
        return switch (category) {
            case NONE -> "#507070";
            case LOW -> "#00a0d0";
            case NORMAL -> "#009030";
            case HIGH -> "#900000";
        };
    }

}
