package com.example.ui.main;

import java.time.format.DateTimeFormatter;

import com.example.model.domain.BmiRecord;
import com.example.ui.I18n;
import com.example.ui.View;

import io.github.sosuisen.jfxbuilder.controls.ButtonBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.ListViewBuilder;
import io.github.sosuisen.jfxbuilder.controls.ScrollPaneBuilder;
import io.github.sosuisen.jfxbuilder.controls.TextFieldBuilder;
import io.github.sosuisen.jfxbuilder.graphics.ColumnConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.GridPaneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.RowConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainView implements View {
    private final String TITLE = "BMI calculator";

    private final MainViewModel viewModel;
    private final Scene scene;

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public MainView(MainViewModel viewModel) {
        this.viewModel = viewModel;
        scene = buildSceneGraph();

        scene.getStylesheets().add("data:text/css;base64," +
                java.util.Base64.getEncoder().encodeToString(mainCSS.getBytes()));

    }

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
            }

            .gridPane {
                -fx-background-color: "#f0f0f0";
                -fx-border-width: 1;
                -fx-border-color: #000000;
                -fx-border-style: solid;
            }
            """;

    private Scene buildSceneGraph() {
        return SceneBuilder
                .create(
                        VBoxBuilder
                                .withChildren(
                                        calculatorPanel(),
                                        historyPanel())
                                .padding(new Insets(3))
                                .style(mainCSS)
                                .build(),
                        240, 550)
                .build();
    }

    private GridPane calculatorPanel() {
        var rowConstraint = RowConstraintsBuilder.create()
                .vgrow(Priority.SOMETIMES)
                .minHeight(30)
                .build();

        return GridPaneBuilder.create()
                .padding(new Insets(3))
                .addRow(0,
                        LabelBuilder.create()
                                .text(I18n.get("main.height"))
                                .hAlignmentInContainer(HPos.CENTER)
                                .build(),
                        TextFieldBuilder.create()
                                .alignment(Pos.CENTER)
                                .marginInContainer(new Insets(3))
                                .build())
                .addRow(1,
                        LabelBuilder.create()
                                .text(I18n.get("main.weight"))
                                .hAlignmentInContainer(HPos.CENTER)
                                .build(),
                        TextFieldBuilder.create()
                                .alignment(Pos.CENTER)
                                .marginInContainer(new Insets(3))
                                .build())
                .addRow(2,
                        LabelBuilder.create()
                                .text(I18n.get("main.bmi"))
                                .hAlignmentInContainer(HPos.CENTER)
                                .build(),
                        LabelBuilder.create()
                                .text("0")
                                .padding(new Insets(3))
                                .marginInContainer(new Insets(3))
                                .alignment(Pos.CENTER)
                                .maxWidth(Double.MAX_VALUE)
                                .build())
                .addRow(3,
                        LabelBuilder.create()
                                .text(I18n.get("main.obesity"))
                                .hAlignmentInContainer(HPos.CENTER)
                                .build(),
                        LabelBuilder.create()
                                .text("-")
                                .padding(new Insets(3))
                                .marginInContainer(new Insets(3))
                                .alignment(Pos.CENTER)
                                .maxWidth(Double.MAX_VALUE)
                                .build())
                .add(
                        ButtonBuilder.create()
                                .text(I18n.get("main.record"))
                                .style("""
                                        -fx-corner-radius: 12px;
                                        """)
                                .hAlignmentInContainer(HPos.CENTER)
                                .build(),
                        0, 4, 2, 1)
                .columnConstraints(
                        ColumnConstraintsBuilder.create()
                                .minWidth(70)
                                .build(),
                        ColumnConstraintsBuilder.create()
                                .hgrow(Priority.ALWAYS)
                                .build())
                .rowConstraints(
                        rowConstraint,
                        rowConstraint,
                        rowConstraint,
                        rowConstraint,
                        rowConstraint)
                .build();
    }

    private ScrollPane historyPanel() {
        return ScrollPaneBuilder.create()
                .fitToHeight(true)
                .fitToWidth(true)
                .content(
                        ListViewBuilder.<BmiRecord>create()
                                .items(viewModel.getBmiList())
                                .cellFactory(this::recordsCellFactory)
                                .build())
                .apply(node -> VBox.setVgrow(node, Priority.ALWAYS))
                .build();
    }

    private ListCell<BmiRecord> recordsCellFactory(ListView<BmiRecord> listView) {
        return new ListCell<BmiRecord>() {
            @Override
            protected void updateItem(BmiRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("[%s] %.1f (%s)",
                            item.datetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            item.bmi(),
                            I18n.get("main.obesity.category." + item.obesity().toString().toLowerCase())));
                }
            }
        };
    }

}
