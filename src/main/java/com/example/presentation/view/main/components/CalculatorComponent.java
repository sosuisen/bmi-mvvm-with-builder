package com.example.presentation.view.main.components;

import com.example.presentation.utils.Formatters;
import com.example.presentation.view.common.I18n;
import com.example.presentation.view.main.MainViewModel;

import io.github.sosuisen.jfxbuilder.controls.ButtonBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.TextFieldBuilder;
import io.github.sosuisen.jfxbuilder.graphics.ColumnConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.GridPaneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.RowConstraintsBuilder;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.converter.NumberStringConverter;

public class CalculatorComponent {
    private static final String mainCSS = """
            .button {
                -fx-background-color: #006000;
                -fx-text-fill: white;
            }

            .button:hover {
                -fx-background-color: #009000;
                -fx-text-fill: white;
                -fx-cursor: hand;
            }

            .label {
                -fx-font-weight: bold;
                -fx-alignment: center;
            }

            .grid-pane {
                -fx-background-color: #f0f0f0;
                -fx-border-width: 1;
                -fx-border-color: black;
                -fx-border-style: solid;
            }

            .text-field {
                -fx-alignment: center;
            }
            """;

    public static GridPane getRoot(MainViewModel viewModel) {
        var rowConstraint = RowConstraintsBuilder.create()
                .vgrow(Priority.SOMETIMES)
                .minHeight(30)
                .maxHeight(50)
                .build();

        return GridPaneBuilder.create()
                .padding(new Insets(3))
                .addRow(0,
                        LabelBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.height")))
                                .hAlignmentInGridPane(HPos.CENTER)
                                .build(),
                        TextFieldBuilder.create()
                                .marginInGridPane(new Insets(3))
                                .textFormatter(Formatters.forNonNegativeNumbers())
                                .textPropertyApply(prop -> prop
                                        .bindBidirectional(viewModel.heightProperty(), new NumberStringConverter()))
                                .build(),
                        LabelBuilder.create()
                                .textPropertyApply(prop -> prop.bind(viewModel.unitSystemProperty()
                                        .map(unitSystem -> unitSystem.getHeightUnit())))
                                .build())
                .addRow(1,
                        LabelBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.weight")))
                                .hAlignmentInGridPane(HPos.CENTER)
                                .build(),
                        TextFieldBuilder.create()
                                .marginInGridPane(new Insets(3))
                                .textFormatter(Formatters.forNonNegativeNumbers())
                                .textPropertyApply(prop -> prop
                                        .bindBidirectional(viewModel.weightProperty(), new NumberStringConverter()))
                                .build(),
                        LabelBuilder.create()
                                .textPropertyApply(prop -> prop.bind(viewModel.unitSystemProperty()
                                        .map(unitSystem -> unitSystem.getWeightUnit())))
                                .build())
                .addRow(2,
                        LabelBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.bmi")))
                                .hAlignmentInGridPane(HPos.CENTER)
                                .build(),
                        LabelBuilder.create()
                                .marginInGridPane(new Insets(3))
                                .maxWidth(Double.MAX_VALUE)
                                .textPropertyApply(prop -> prop
                                        .bind(viewModel.bmiProperty()
                                                .map(opt -> opt
                                                        .map(bmi -> String.format("%.1f", bmi))
                                                        .orElse("-"))))
                                .columnSpanInGridPane(2)
                                .build())
                .addRow(3,
                        LabelBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.obesity")))
                                .hAlignmentInGridPane(HPos.CENTER)
                                .build(),
                        LabelBuilder.create()
                                .marginInGridPane(new Insets(3))
                                .maxWidth(Double.MAX_VALUE)
                                .textPropertyApply(prop -> prop
                                        .bind(Bindings.createStringBinding(() -> viewModel.obesityProperty()
                                                .map(opt -> opt.map(
                                                        category -> I18n.text("main.obesity.category." + category))
                                                        .orElse("-"))
                                                .getValue(),
                                                viewModel.obesityProperty(),
                                                I18n.INSTANCE.resourcesProperty())))
                                .columnSpanInGridPane(2)
                                .build())
                .add(
                        ButtonBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.record")))
                                .style("""
                                        -fx-corner-radius: 12px;
                                        """)
                                .hAlignmentInGridPane(HPos.CENTER)
                                .disablePropertyApply(prop -> prop.bind(viewModel.bmiProperty()
                                        .map(opt -> !opt.isPresent() || opt.isEmpty())))
                                .onAction(_ -> viewModel.saveBmiRecord())
                                .build(),
                        0, 4, 3, 1)
                .observableColumnConstraints(
                        ColumnConstraintsBuilder.create()
                                .minWidth(70)
                                .prefWidth(70)
                                .build(),
                        ColumnConstraintsBuilder.create()
                                .minWidth(60)
                                .prefWidth(60)
                                .build(),
                        ColumnConstraintsBuilder.create()
                                .minWidth(40)
                                .prefWidth(40)
                                .build())
                .observableRowConstraints(
                        rowConstraint,
                        rowConstraint,
                        rowConstraint,
                        rowConstraint,
                        rowConstraint)
                .apply(node -> node.getStylesheets().add("data:text/css;base64," +
                        java.util.Base64.getEncoder().encodeToString(mainCSS.getBytes())))

                .build();
    }
}
