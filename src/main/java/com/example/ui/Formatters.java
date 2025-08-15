package com.example.ui;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.DefaultStringConverter;

public class Formatters {
    public static TextFormatter<String> forNonNegativeNumbers() {
        return new TextFormatter<>(
                new DefaultStringConverter(),
                "", // 空文字列で初期化
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.isEmpty())
                        return change;
                    // 0以上の数。小数点以下も認める。
                    if (newText.matches("^(0|[1-9]\\d*(\\.\\d*)?|0\\.\\d*)$"))
                        return change;
                    return null;
                });
    }
}