package com.example.presentation.utils;

import java.util.function.Function;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class TableRowFactories {
    public static <S> Callback<TableView<S>, TableRow<S>> createColoredRowFactory(
            Function<S, String> webColorConverter) {
        return _ -> new TableRow<S>() {
            @Override
            protected void updateItem(S item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle(null);
                } else {
                    var style = "-fx-background-color: %s;"
                            .formatted(webColorConverter.apply(item));
                    setStyle(style);
                }
            }
        };
    }
}
