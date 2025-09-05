package com.example.presentation.screens.main.components;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.domain.model.ObesityCategory;
import com.example.domain.service.BmiRecordWithDiff;
import com.example.presentation.helpers.I18n;
import com.example.presentation.helpers.TableCellFactories;
import com.example.presentation.helpers.TableCellValueFactories;
import com.example.presentation.helpers.TableRowFactories;
import com.example.presentation.screens.main.MainViewModel;
import com.example.presentation.styles.ObesityColor;

import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.TableColumnBuilder;
import io.github.sosuisen.jfxbuilder.controls.TableViewBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HistoryTableComponent {
    public static VBox getRoot(MainViewModel viewModel) {
        return VBoxBuilder
            .withChildren(
                LabelBuilder.create()
                    .textPropertyApply(
                        prop -> prop.bind(I18n.textProperty("history.table.title"))
                    )
                    .build(),
                TableViewBuilder.<BmiRecordWithDiff>create()
                    .items(viewModel.getBmiList())
                    .addColumns(
                        TableColumnBuilder.<BmiRecordWithDiff, LocalDate>create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.table.date"))
                            )
                            .cellValueFactory(
                                TableCellValueFactories
                                    .createReadOnlyCellValueFactory(BmiRecordWithDiff::date)
                            )
                            .cellFactory(
                                TableCellFactories.createTextCellFactory(
                                    item -> item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                )
                            )
                            .style("-fx-alignment: center")
                            .prefWidth(100)
                            .build(),
                        TableColumnBuilder.<BmiRecordWithDiff, Double>create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.table.height"))
                            )
                            .cellValueFactory(
                                TableCellValueFactories
                                    .createReadOnlyCellValueFactory(BmiRecordWithDiff::heightMeter)
                            )
                            .cellFactory(
                                TableCellFactories
                                    .createTextCellFactory(
                                        item -> String.format(
                                            "%.1f", viewModel.convertHeightFromSI(item)
                                        )
                                    )
                            )
                            .style("-fx-alignment: center-right")
                            .build(),
                        TableColumnBuilder.<BmiRecordWithDiff, Double>create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.table.weight"))
                            )
                            .cellValueFactory(
                                TableCellValueFactories
                                    .createReadOnlyCellValueFactory(BmiRecordWithDiff::weightKg)
                            )
                            .cellFactory(
                                TableCellFactories
                                    .createTextCellFactory(
                                        item -> String.format(
                                            "%.1f", viewModel.convertWeightFromSI(item)
                                        )
                                    )
                            )
                            .style("-fx-alignment: center-right")
                            .build(),
                        TableColumnBuilder.<BmiRecordWithDiff, Double>create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.table.bmi"))
                            )
                            .cellValueFactory(
                                TableCellValueFactories
                                    .createReadOnlyCellValueFactory(BmiRecordWithDiff::bmi)
                            )
                            .cellFactory(
                                TableCellFactories
                                    .createTextCellFactory(item -> String.format("%.1f", item))
                            )
                            .style("-fx-alignment: center-right")
                            .build(),
                        TableColumnBuilder.<BmiRecordWithDiff, ObesityCategory>create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("history.table.obesity"))
                            )
                            .cellValueFactory(
                                TableCellValueFactories
                                    .createReadOnlyCellValueFactory(BmiRecordWithDiff::obesity)
                            )
                            .cellFactory(
                                TableCellFactories
                                    .createTextCellFactory(
                                        item -> I18n.text(
                                            "main.obesity.category." + item.toResourceString()
                                        )
                                    )
                            )
                            .style("-fx-alignment: center")
                            .prefWidth(100)
                            .build(),
                        TableColumnBuilder.<BmiRecordWithDiff, Integer>create()
                            .cellValueFactory(
                                TableCellValueFactories
                                    .createReadOnlyCellValueFactory(BmiRecordWithDiff::id)
                            )
                            .cellFactory(
                                TableCellFactories
                                    .createButtonCellFactory(
                                        viewModel::removeRecord,
                                        "history.table.delete",
                                        new String[] {"button-danger", "button-small"}
                                    )
                            )
                            .style("-fx-alignment: center;")
                            .prefWidth(50)
                            .build()
                    )
                    .rowFactory(
                        TableRowFactories.createColoredRowFactory(
                            record -> ObesityColor.getLightColor(record.obesity())
                        )
                    )
                    .vGrowInVBox(Priority.ALWAYS)
                    .build()
            )
            .padding(new Insets(3))
            .build();
    }

}
