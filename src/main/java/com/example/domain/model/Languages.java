package com.example.domain.model;

import java.util.Arrays;
import java.util.List;

public enum Languages {
    EN,
    JA;

    public static List<Languages> getLanguageList() {
        return Arrays.asList(Languages.values());
    }

    public static Languages getLanguage(String value) {
        return Languages.valueOf(value.toUpperCase());
    }

    public static Languages getDefaultLanguages() {
        return EN;
    }

    public String toLanguageString() {
        return toString().toLowerCase();
    }
}
