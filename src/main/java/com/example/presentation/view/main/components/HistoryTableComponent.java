package com.example.presentation.view.main.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.domain.model.BmiRecord;
import com.example.domain.model.ObesityCategory;
import com.example.presentation.utils.TableCellFactories;
import com.example.presentation.utils.TableCellValueFactories;
import com.example.presentation.view.common.I18n;
import com.example.presentation.view.main.MainViewModel;

import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.TableColumnBuilder;
import io.github.sosuisen.jfxbuilder.controls.TableViewBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HistoryTableComponent {
    public static VBox getRoot(MainViewModel viewModel) {
        return VBoxBuilder.withChildren(
                LabelBuilder.create()
                        .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.table.title")))
                        .build(),
                TableViewBuilder.<BmiRecord>create()
                        .items(viewModel.getBmiList())
                        .addColumns(
                                TableColumnBuilder.<BmiRecord, LocalDate>create()
                                        .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.table.date")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.date()))
                                        .cellFactory(TableCellFactories.createCellFactory(
                                                item -> item.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"))))
                                        .style("-fx-alignment: center")
                                        .prefWidth(100)
                                        .build(),
                                TableColumnBuilder.<BmiRecord, Double>create()
                                        .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.table.height")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.heightMeter()))
                                        .cellFactory(TableCellFactories
                                                .createCellFactory(item -> String.format("%.1f",
                                                        viewModel.convertHeightFromSI(item.doubleValue()))))
                                        .style("-fx-alignment: center-right")
                                        .build(),
                                TableColumnBuilder.<BmiRecord, Double>create()
                                        .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.table.weight")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.weightKg()))
                                        .cellFactory(TableCellFactories
                                                .createCellFactory(item -> String.format("%.1f",
                                                        viewModel.convertWeightFromSI(item.doubleValue()))))
                                        .style("-fx-alignment: center-right")
                                        .build(),
                                TableColumnBuilder.<BmiRecord, Double>create()
                                        .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.table.bmi")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.bmi()))
                                        .cellFactory(TableCellFactories
                                                .createCellFactory(item -> String.format("%.1f", item.doubleValue())))
                                        .style("-fx-alignment: center-right")
                                        .build(),
                                TableColumnBuilder.<BmiRecord, ObesityCategory>create()
                                        .textPropertyApply(
                                                prop -> prop.bind(I18n.textProperty("history.table.obesity")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.obesity()))
                                        .cellFactory(TableCellFactories
                                                .createCellFactory(item -> I18n
                                                        .text("main.obesity.category." + item.toResourceString())))
                                        .style("-fx-alignment: center")
                                        .prefWidth(100)
                                        .build())
                        .vGrowInVBox(Priority.ALWAYS)
                        .build())
                .padding(new Insets(3))
                .build();
    }

}
