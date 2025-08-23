package com.example.presentation.utils;

import java.util.function.Consumer;
import java.util.function.Function;

import com.example.presentation.view.common.I18n;

import io.github.sosuisen.jfxbuilder.controls.ButtonBuilder;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TableCellFactories {
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> createTextCellFactory(
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

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> createButtonCellFactory(
            Consumer<T> callback, String resourceKey, String[] styleClass) {
        return _ -> new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(
                            ButtonBuilder.create()
                                    .textPropertyApply(prop -> prop.bind(I18n.textProperty(resourceKey)))
                                    .onAction(_ -> callback.accept(item))
                                    .addStyleClass(styleClass)
                                    .build());
                }
            }
        };
    }
}
