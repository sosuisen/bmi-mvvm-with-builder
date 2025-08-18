package com.example.model.domain;

public class Obesity {
    public enum Category {
        NONE, LOW, NORMAL, HIGH
    }

    public static Category getCategory(double bmi) {
        if (bmi <= 0) {
            return Category.NONE;
        } else if (bmi < 18.5) {
            return Category.LOW;
        } else if (bmi < 25.0) {
            return Category.NORMAL;
        } else {
            return Category.HIGH;
        }
    }

}
