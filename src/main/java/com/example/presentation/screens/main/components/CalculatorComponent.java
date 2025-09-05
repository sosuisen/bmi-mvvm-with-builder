package com.example.presentation.screens.main.components;

import com.example.domain.model.unit.UnitSystem;
import com.example.presentation.helpers.Formatters;
import com.example.presentation.helpers.I18n;
import com.example.presentation.screens.main.MainViewModel;

import io.github.sosuisen.jfxbuilder.controls.ButtonBuilder;
import io.github.sosuisen.jfxbuilder.controls.DatePickerBuilder;
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

import java.util.Optional;

public class CalculatorComponent {
    private static final String CSS =
        """
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
            .addStylesheetsText(CSS)
            .style("""
                   -fx-background-color: #f0f0f0;
                   -fx-border-width: 1;
                   -fx-border-color: black;
                   -fx-border-style: solid;
                   """)
            .addRow(
                0,
                LabelBuilder.create()
                    .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.height")))
                    .hAlignmentInGridPane(HPos.CENTER)
                    .addStyleClass("label-bold")
                    .build(),
                TextFieldBuilder.create()
                    .marginInGridPane(new Insets(3))
                    .textFormatter(Formatters.forNonNegativeNumbers())
                    .textPropertyApply(
                        prop -> prop.bindBidirectional(
                            viewModel.heightProperty(), new NumberStringConverter()
                        )
                    )
                    .build(),
                LabelBuilder.create()
                    .textPropertyApply(
                        prop -> prop.bind(
                            viewModel.unitSystemProperty().map(UnitSystem::getHeightUnit)
                        )
                    )
                    .build()
            )
            .addRow(
                1,
                LabelBuilder.create()
                    .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.weight")))
                    .hAlignmentInGridPane(HPos.CENTER)
                    .addStyleClass("label-bold")
                    .build(),
                TextFieldBuilder.create()
                    .marginInGridPane(new Insets(3))
                    .textFormatter(Formatters.forNonNegativeNumbers())
                    .textPropertyApply(
                        prop -> prop.bindBidirectional(
                            viewModel.weightProperty(), new NumberStringConverter()
                        )
                    )
                    .build(),
                LabelBuilder.create()
                    .textPropertyApply(
                        prop -> prop.bind(
                            viewModel.unitSystemProperty().map(UnitSystem::getWeightUnit)
                        )
                    )
                    .build()
            )
            .addRow(
                2,
                LabelBuilder.create()
                    .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.bmi")))
                    .hAlignmentInGridPane(HPos.CENTER)
                    .addStyleClass("label-bold")
                    .build(),
                LabelBuilder.create()
                    .marginInGridPane(new Insets(3))
                    .maxWidth(Double.MAX_VALUE)
                    .textPropertyApply(
                        prop -> prop.bind(
                            viewModel.bmiProperty().map(
                                opt -> opt.map(bmi -> String.format("%.1f", bmi)).orElse("-")
                            )
                        )
                    )
                    .build()
            )
            .addRow(
                3,
                LabelBuilder.create()
                    .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.obesity")))
                    .hAlignmentInGridPane(HPos.CENTER)
                    .addStyleClass("label-bold")
                    .build(),
                LabelBuilder.create()
                    .marginInGridPane(new Insets(3))
                    .maxWidth(Double.MAX_VALUE)
                    .textPropertyApply(
                        prop -> prop.bind(
                            Bindings.createStringBinding(
                                () -> viewModel.obesityProperty().map(
                                    opt -> opt.map(
                                        category -> I18n.text("main.obesity.category." + category)
                                    ).orElse("-")
                                ).getValue(),
                                viewModel.obesityProperty(),
                                I18n.INSTANCE.resourcesProperty()
                            )
                        )
                    )
                    .build()
            )
            // row 4
            // Cannot use addRow when the first column has column span.
            .addChildren(
                DatePickerBuilder.create()
                    .rowIndexInGridPane(4)
                    .columnIndexInGridPane(0)
                    .columnSpanInGridPane(2)
                    .valuePropertyApply(prop -> prop.bindBidirectional(viewModel.dateProperty()))
                    .hAlignmentInGridPane(HPos.CENTER)
                    .marginInGridPane(new Insets(3))
                    .build(),
                ButtonBuilder.create()
                    .rowIndexInGridPane(4)
                    .columnIndexInGridPane(2)
                    .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.today")))
                    .onAction((_ -> viewModel.setToday()))
                    .hAlignmentInGridPane(HPos.CENTER)
                    .build()
            )
            .addRow(
                5,
                ButtonBuilder.create()
                    .columnSpanInGridPane(3)
                    .textPropertyApply(prop -> prop.bind(I18n.textProperty("main.record")))
                    .style("""
                           -fx-corner-radius: 12px;
                           """)
                    .addStyleClass("button-safe")
                    .hAlignmentInGridPane(HPos.CENTER)
                    .disablePropertyApply(
                        prop -> prop.bind(viewModel.bmiProperty().map(Optional::isEmpty))
                    )
                    .onAction(_ -> viewModel.saveBmiRecord())
                    .build()
            )
            .addColumnConstraints(
                ColumnConstraintsBuilder.create()
                    .minWidth(70)
                    .prefWidth(70)
                    .build(),
                ColumnConstraintsBuilder.create()
                    .minWidth(90)
                    .prefWidth(90)
                    .build(),
                ColumnConstraintsBuilder.create()
                    .minWidth(50)
                    .prefWidth(50)
                    .build()
            )
            .addRowConstraints(
                rowConstraint,
                rowConstraint,
                rowConstraint,
                rowConstraint,
                rowConstraint,
                rowConstraint
            )
            .build();
    }
}
