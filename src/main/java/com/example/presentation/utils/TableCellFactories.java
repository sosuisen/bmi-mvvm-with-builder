package com.example.presentation.utils;

import java.util.function.Function;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TableCellFactories {
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> createCellFactory(
            Function<T, String> formatter) {
        return _ -> new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.apply(item));
                }
            }
        };
    }
}
