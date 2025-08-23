package com.example.presentation.view.styles;

import com.example.domain.model.ObesityCategory;

public class ObesityColor {
    /*
     * Get web-style color string. E.g) "#ffff00"
     */
    public static String getLightColor(ObesityCategory category) {
        return switch (category) {
            case NONE -> "#90a0a0";
            case LOW -> "#ff9050";
            case NORMAL -> "#50f0a0";
            case HIGH -> "#ff5050";
        };
    }

    public static String getDarkColor(ObesityCategory category) {
        return switch (category) {
            case NONE -> "#507070";
            case LOW -> "#d05000";
            case NORMAL -> "#009030";
            case HIGH -> "#900000";
        };
    }

}
