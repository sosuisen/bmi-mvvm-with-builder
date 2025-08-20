package com.example.domain.model;

import java.util.Arrays;
import java.util.List;

public enum Languages {
    EN,
    JA;

    public static List<Languages> getValues() {
        return Arrays.asList(Languages.values());
    }

    public static Languages fromString(String value) {
        return Languages.valueOf(value.toUpperCase());
    }
}
