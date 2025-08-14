package com.example.ui.main;

import java.time.format.DateTimeFormatter;

import com.example.model.domain.BmiRecord;
import com.example.ui.I18n;
import com.example.ui.View;

import io.github.sosuisen.jfxbuilder.controls.ButtonBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.ListViewBuilder;
import io.github.sosuisen.jfxbuilder.controls.ScrollBarBuilder;
import io.github.sosuisen.jfxbuilder.controls.ScrollPaneBuilder;
import io.github.sosuisen.jfxbuilder.controls.TextFieldBuilder;
import io.github.sosuisen.jfxbuilder.graphics.ColumnConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.GridPaneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.RowConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
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
    }

    private Scene buildSceneGraph() {
        return SceneBuilder
                .create(
                        VBoxBuilder
                                .withChildren(
                                        calculatorPanel(),
                                        historyPanel())
                                .build(),
                        240, 480)
                .build();
    }

    private GridPane calculatorPanel() {
        var rowConstraint = RowConstraintsBuilder.create()
                .vgrow(Priority.SOMETIMES)
                .minHeight(30)
                .build();
        return GridPaneBuilder.create()
                .addRow(0,
                        LabelBuilder.create()
                                .text(I18n.get("main.height"))
                                .alignment(Pos.CENTER)
                                .build(),
                        TextFieldBuilder.create()
                                .alignment(Pos.CENTER)
                                .build())
                .addRow(1,
                        LabelBuilder.create()
                                .text(I18n.get("main.weight"))
                                .alignment(Pos.CENTER)
                                .build(),
                        TextFieldBuilder.create()
                                .alignment(Pos.CENTER)
                                .build())
                .addRow(2,
                        LabelBuilder.create()
                                .text(I18n.get("main.bmi"))
                                .alignment(Pos.CENTER)
                                .build(),
                        LabelBuilder.create()
                                .text("0")
                                .alignment(Pos.CENTER)
                                .build())
                .addRow(3,
                        LabelBuilder.create()
                                .text(I18n.get("main.obesity"))
                                .alignment(Pos.CENTER)
                                .build(),
                        LabelBuilder.create()
                                .text("-")
                                .alignment(Pos.CENTER)
                                .build())
                .addRow(4,
                        ButtonBuilder.create()
                                .text("main.record")
                                .alignment(Pos.CENTER)
                                .build())
                .columnConstraints(
                        ColumnConstraintsBuilder.create()
                                .hgrow(Priority.SOMETIMES)
                                .maxWidth(295)
                                .minWidth(10)
                                .prefWidth(65)
                                .build(),
                        ColumnConstraintsBuilder.create()
                                .hgrow(Priority.SOMETIMES)
                                .maxWidth(542)
                                .minWidth(10)
                                .prefWidth(115)
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
