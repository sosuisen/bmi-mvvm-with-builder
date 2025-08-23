package com.example.domain.model;

import java.time.LocalDate;

public interface Bmi {
    int id();

    LocalDate date();

    double heightMeter();

    double weightKg();

    double bmi();

    ObesityCategory obesity();
}
