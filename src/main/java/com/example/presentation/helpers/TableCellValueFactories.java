package com.example.presentation.helpers;

import java.util.function.Function;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class TableCellValueFactories {
    /*
     * Return a CellValueFactory that converts a record to the cell's type.
     * 
     * PropertyValueFactory can only manage a property or a non-property field with
     * a traditional JavaBeans getter (getXXX or isXXX method).
     * With PropertyValueFactory, changes are bidirectional for property fields and
     * unidirectional for non-property fields.
     * PropertyValueFactory cannot handle a record class.
     * createReadOnlyCellValueFactory can manage a record class using a converter
     * function to convert a record to the cell's type.
     * Changes are naturally unidirectional for a record field.
     */
    public static <S, T> Callback<CellDataFeatures<S, T>, ObservableValue<T>> createReadOnlyCellValueFactory(
            Function<S, T> converter) {
        return new Callback<CellDataFeatures<S, T>, ObservableValue<T>>() {
            public ObservableValue<T> call(CellDataFeatures<S, T> dataFeatures) {
                return new ReadOnlyObjectWrapper<T>(converter.apply(dataFeatures.getValue()));
            }
        };
    }
}
