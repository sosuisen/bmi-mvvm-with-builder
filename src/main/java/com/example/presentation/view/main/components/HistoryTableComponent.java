package com.example.presentation.view.main.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.domain.model.ObesityCategory;
import com.example.domain.service.BmiRecordWithDiff;
import com.example.presentation.utils.I18n;
import com.example.presentation.utils.TableCellFactories;
import com.example.presentation.utils.TableCellValueFactories;
import com.example.presentation.utils.TableRowFactories;
import com.example.presentation.view.main.MainViewModel;
import com.example.presentation.view.styles.ObesityColor;

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
                TableViewBuilder.withItems(
                        viewModel.getBmiList())
                        .addColumns(
                                TableColumnBuilder.<BmiRecordWithDiff, LocalDate>create()
                                        .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.table.date")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.date()))
                                        .cellFactory(TableCellFactories.createTextCellFactory(
                                                item -> item.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"))))
                                        .style("-fx-alignment: center")
                                        .prefWidth(100)
                                        .build(),
                                TableColumnBuilder.<BmiRecordWithDiff, Double>create()
                                        .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.table.height")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.heightMeter()))
                                        .cellFactory(TableCellFactories
                                                .createTextCellFactory(item -> String.format("%.1f",
                                                        viewModel.convertHeightFromSI(item.doubleValue()))))
                                        .style("-fx-alignment: center-right")
                                        .build(),
                                TableColumnBuilder.<BmiRecordWithDiff, Double>create()
                                        .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.table.weight")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.weightKg()))
                                        .cellFactory(TableCellFactories
                                                .createTextCellFactory(item -> String.format("%.1f",
                                                        viewModel.convertWeightFromSI(item.doubleValue()))))
                                        .style("-fx-alignment: center-right")
                                        .build(),
                                TableColumnBuilder.<BmiRecordWithDiff, Double>create()
                                        .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.table.bmi")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.bmi()))
                                        .cellFactory(TableCellFactories
                                                .createTextCellFactory(
                                                        item -> String.format("%.1f", item.doubleValue())))
                                        .style("-fx-alignment: center-right")
                                        .build(),
                                TableColumnBuilder.<BmiRecordWithDiff, ObesityCategory>create()
                                        .textPropertyApply(
                                                prop -> prop.bind(I18n.textProperty("history.table.obesity")))
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.obesity()))
                                        .cellFactory(TableCellFactories
                                                .createTextCellFactory(item -> I18n
                                                        .text("main.obesity.category." + item.toResourceString())))
                                        .style("-fx-alignment: center")
                                        .prefWidth(100)
                                        .build(),
                                TableColumnBuilder.<BmiRecordWithDiff, Integer>create()
                                        .cellValueFactory(TableCellValueFactories
                                                .createReadOnlyCellValueFactory(record -> record.id()))
                                        .cellFactory(TableCellFactories
                                                .createButtonCellFactory(
                                                        item -> viewModel.removeRecord(item),
                                                        "history.table.delete",
                                                        new String[] { "button-danger", "button-small" }))
                                        .style("-fx-alignment: center;")
                                        .prefWidth(50)
                                        .build())
                        .rowFactory(TableRowFactories.<BmiRecordWithDiff>createColoredRowFactory(
                                record -> ObesityColor.getLightColor(record.obesity())))
                        .vGrowInVBox(Priority.ALWAYS)
                        .build())
                .padding(new Insets(3))
                .build();
    }

}
