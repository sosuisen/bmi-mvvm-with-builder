package com.example.ui;

import java.util.Arrays;
import java.util.List;

public enum Languages {
    EN,
    JP;

    public static List<Languages> getValues() {
        return Arrays.asList(Languages.values());
    }
}
