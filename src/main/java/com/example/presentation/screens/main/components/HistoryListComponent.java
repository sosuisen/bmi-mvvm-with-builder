package com.example.presentation.screens.main.components;

import com.example.domain.model.ObesityCategory;
import com.example.domain.service.BmiRecordWithDiff;
import com.example.presentation.helpers.I18n;
import com.example.presentation.screens.main.MainViewModel;
import com.example.presentation.styles.ObesityColor;

import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.ListViewBuilder;
import io.github.sosuisen.jfxbuilder.graphics.HBoxBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class HistoryListComponent {
    public static VBox getRoot(MainViewModel viewModel) {
        return VBoxBuilder
            .withChildren(
                LabelBuilder.create()
                    .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.list.title")))
                    .build(),
                ListViewBuilder.<BmiRecordWithDiff>create()
                    .items(viewModel.getBmiList())
                    .cellFactory(HistoryListComponent::recordsCellFactory)
                    .vGrowInVBox(Priority.ALWAYS)
                    .build()
            )
            .padding(new Insets(3))
            .build();

    }

    private static ListCell<BmiRecordWithDiff> recordsCellFactory(
        ListView<BmiRecordWithDiff> listView) {
        return new ListCell<BmiRecordWithDiff>() {
            @Override
            protected void updateItem(BmiRecordWithDiff item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    var row = HBoxBuilder
                        .withChildren(
                            createBmiLabel(item),
                            createDetailBox(item),
                            createDateLabel(item)
                        )
                        .build();

                    setGraphic(row);
                }
            }
        };
    }

    private static Label createBmiLabel(BmiRecordWithDiff bmiRecord) {
        final double labelRadius = 18;

        return LabelBuilder.create()
            .text(String.format("%.1f", bmiRecord.bmi()))
            .stylePropertyApply(
                prop -> prop.bind(
                    new AnimatedBmiLabelStyle(bmiRecord.obesity(), labelRadius)
                        .getAnimatedStringBinding()
                )
            )
            .prefHeight(labelRadius * 2)
            .prefWidth(labelRadius * 2)
            .marginInHBox(new Insets(7, 20, 7, 10))
            .build();
    }

    private static VBox createDetailBox(BmiRecordWithDiff bmiRecord) {
        var headlineStyle =
            """
            -fx-font-weight: bold;
            -fx-font-size: 16;
            """
                + "-fx-text-fill: %s;"
                    .formatted(ObesityColor.getDarkColor(bmiRecord.obesity()));

        return VBoxBuilder
            .withChildren(
                LabelBuilder.create()
                    .textPropertyApply(
                        prop -> prop.bind(
                            I18n.textProperty(
                                "main.obesity.category." + bmiRecord.obesity().toResourceString()
                            )
                        )
                    )
                    .style(headlineStyle)
                    .build(),
                LabelBuilder.create()
                    .textPropertyApply(
                        prop -> prop.bind(
                            Bindings.createStringBinding(
                                () -> {
                                    String text = I18n.textProperty(
                                        "history.trend." + bmiRecord.trendDescription().toResourceString()
                                    ).get();
                                    return bmiRecord.trendDescription() == BmiRecordWithDiff.Trend.NONE
                                        ? text
                                        : "%s%.1f".formatted(text, bmiRecord.diff());
                                },
                                I18n.textProperty(
                                    "history.trend." + bmiRecord.trendDescription().toResourceString()
                                )
                            )
                        )
                    )
                    .style("-fx-font-size: 12;")
                    .build()
            )
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


class AnimatedBmiLabelStyle {
    private final DoubleProperty cornerRadiusProperty;
    private final DoubleProperty insetProperty;

    private final StringBinding styleBinding;

    private final Animation animation;

    private final ObesityCategory obesity;

    public AnimatedBmiLabelStyle(ObesityCategory obesity, double labelRadius) {
        this.obesity = obesity;

        // When the corner radius decreases, the appearance looks fat.
        double cornerRadiusTransTo;
        switch (obesity) {
            case HIGH:
                cornerRadiusTransTo = 10;
                break;
            case NORMAL:
            case LOW:
            case NONE:
                cornerRadiusTransTo = 18;
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + obesity);
        }

        double insetTransFrom = 0;

        // When the corner radius increases, the appearance looks thin.
        double insetTransTo;
        switch (obesity) {
            case HIGH:
            case NORMAL:
            case NONE:
                insetTransTo = 0;
                break;
            case LOW:
                insetTransTo = 2;
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + obesity);
        }

        cornerRadiusProperty = new SimpleDoubleProperty(labelRadius);
        insetProperty = new SimpleDoubleProperty(insetTransFrom);

        styleBinding = Bindings.createStringBinding(
            () -> "-fx-background-color: %s;".formatted(ObesityColor.getDarkColor(obesity)) +
                "-fx-background-radius: %s;".formatted(cornerRadiusProperty.get()) +
                "-fx-background-insets: %s;".formatted(insetProperty.get()) +
                """
                -fx-font-weight: bold;
                -fx-alignment: center;
                -fx-text-fill: white;
                """,
            cornerRadiusProperty, insetProperty
        );

        animation = new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(cornerRadiusProperty, labelRadius),
                new KeyValue(insetProperty, insetTransFrom)
            ),
            new KeyFrame(
                Duration.seconds(0.6),
                new KeyValue(cornerRadiusProperty, cornerRadiusTransTo),
                new KeyValue(insetProperty, insetTransTo)
            ),
            new KeyFrame(
                Duration.seconds(1.8),
                new KeyValue(cornerRadiusProperty, labelRadius),
                new KeyValue(insetProperty, insetTransFrom)
            )
        );
        animation.setCycleCount(Timeline.INDEFINITE);

    }

    public StringBinding getAnimatedStringBinding() {
        if (obesity.equals(ObesityCategory.HIGH) || obesity.equals(ObesityCategory.LOW)) {
            animation.play();
        }
        return styleBinding;
    }
}
