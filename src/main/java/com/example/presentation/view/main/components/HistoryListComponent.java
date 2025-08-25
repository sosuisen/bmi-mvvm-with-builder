package com.example.presentation.view.main.components;

import com.example.domain.service.BmiRecordWithDiff;
import com.example.presentation.utils.I18n;
import com.example.presentation.view.main.MainViewModel;
import com.example.presentation.view.styles.ObesityColor;

import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.ListViewBuilder;
import io.github.sosuisen.jfxbuilder.graphics.HBoxBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HistoryListComponent {
    public static VBox getRoot(MainViewModel viewModel) {
        return VBoxBuilder
                .withChildren(
                        LabelBuilder.create()
                                .textPropertyApply(
                                        prop -> prop.bind(I18n.textProperty("history.list.title")))
                                .build(),
                        ListViewBuilder.<BmiRecordWithDiff>create()
                                .items(viewModel.getBmiList())
                                .cellFactory(HistoryListComponent::recordsCellFactory)
                                .vGrowInVBox(Priority.ALWAYS)
                                .build())
                .padding(new Insets(3))
                .build();

    }

    private static ListCell<BmiRecordWithDiff> recordsCellFactory(ListView<BmiRecordWithDiff> listView) {
        return new ListCell<BmiRecordWithDiff>() {
            @Override
            protected void updateItem(BmiRecordWithDiff item, boolean empty) {
                super.updateItem(item, empty);

                textProperty().unbind();

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    var row = HBoxBuilder
                            .withChildren(
                                    createBmiLabel(item),
                                    createDetailBox(item),
                                    createDateLabel(item))
                            .build();

                    setGraphic(row);
                }
            }
        };
    }

    private static Label createBmiLabel(BmiRecordWithDiff bmiRecord) {
        int labelRadius = 18;
        var labelStyle = new StringBuilder()
                .append("-fx-background-color: %s;"
                        .formatted(ObesityColor.getDarkColor(bmiRecord.obesity())))
                .append("-fx-background-radius: %s;"
                        .formatted(labelRadius))
                .append("""
                        -fx-font-weight: bold;
                        -fx-alignment: center;
                        -fx-text-fill: white;
                        """)
                .toString();
        return LabelBuilder.create()
                .text(String.format("%.1f", bmiRecord.bmi()))
                .style(labelStyle)
                .prefHeight(labelRadius * 2)
                .prefWidth(labelRadius * 2)
                .marginInHBox(new Insets(7, 20, 7, 10))
                .build();

    }

    private static VBox createDetailBox(BmiRecordWithDiff bmiRecord) {
        var headlineStyle = new StringBuilder()
                .append("""
                        -fx-font-weight: bold;
                        -fx-font-size: 16;
                        """)
                .append("-fx-text-fill: %s;"
                        .formatted(ObesityColor.getDarkColor(bmiRecord.obesity())))
                .toString();

        return VBoxBuilder.withChildren(
                LabelBuilder.create()
                        .textPropertyApply(
                                prop -> prop.bind(I18n
                                        .textProperty(
                                                "main.obesity.category." + bmiRecord.obesity().toResourceString())))
                        .style(headlineStyle)
                        .build(),
                LabelBuilder.create()
                        .textPropertyApply(
                                prop -> prop.bind(I18n
                                        .textProperty(
                                                "history.trend."
                                                        + bmiRecord.trendDescription().toResourceString())
                                        .map(text -> bmiRecord.trendDescription() == BmiRecordWithDiff.Trend.NONE
                                                ? text
                                                : "%s%.1f".formatted(text, bmiRecord.diff()))))
                        .style("-fx-font-size: 12;")
                        .build())
                .build();
    }

    private static Label createDateLabel(BmiRecordWithDiff bmiRecord) {
        return LabelBuilder.create()
                .text(bmiRecord.date().toString())
                .style("""
                        -fx-font-size: 12;
                        -fx-alignment: center-right;
                        """)
                .maxWidth(Double.MAX_VALUE)
                .hGrowInHBox(Priority.ALWAYS)
                .build();

    }

}
