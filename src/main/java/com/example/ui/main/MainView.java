package com.example.ui.main;

import java.time.format.DateTimeFormatter;

import com.example.model.domain.BmiRecord;
import com.example.ui.Formatters;
import com.example.ui.I18n;
import com.example.ui.View;

import io.github.sosuisen.jfxbuilder.controls.AlertBuilder;
import io.github.sosuisen.jfxbuilder.controls.ButtonBuilder;
import io.github.sosuisen.jfxbuilder.controls.ButtonTypeBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.ListViewBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuBarBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuItemBuilder;
import io.github.sosuisen.jfxbuilder.controls.ScrollPaneBuilder;
import io.github.sosuisen.jfxbuilder.controls.TextFieldBuilder;
import io.github.sosuisen.jfxbuilder.graphics.ColumnConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.GridPaneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.RowConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.StageBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.converter.NumberStringConverter;

public class MainView implements View {
    private final String TITLE = "BMI Calc";

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

    private Scene buildSceneGraph() {
        return SceneBuilder
                .withRoot(
                        VBoxBuilder
                                .withChildren(
                                        menuBar(),
                                        calculatorPanel(),
                                        historyPanel())
                                .padding(new Insets(3))
                                .build())
                .width(240)
                .height(450)
                .addStylesheetText(mainCSS)
                .build();
    }

    private MenuBar menuBar() {
        return MenuBarBuilder.create()
                .addMenus(
                        MenuBuilder.create()
                                .text(I18n.get("menu.file"))
                                .addItems(
                                        MenuItemBuilder.create()
                                                .text(I18n.get("menu.settings"))
                                                .onAction(_ -> openSettingsWindow())
                                                .build(),
                                        MenuItemBuilder.create()
                                                .text(I18n.get("menu.close"))
                                                .onAction(_ -> Platform.exit())
                                                .build())
                                .build())
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
                                .hAlignmentInGridPane(HPos.CENTER)
                                .build(),
                        TextFieldBuilder.create()
                                .marginInGridPane(new Insets(3))
                                .textFormatter(Formatters.forNonNegativeNumbers())
                                .textPropertyApply(prop -> prop
                                        .bindBidirectional(viewModel.heightProperty(), new NumberStringConverter()))
                                .build())
                .addRow(1,
                        LabelBuilder.create()
                                .text(I18n.get("main.weight"))
                                .hAlignmentInGridPane(HPos.CENTER)
                                .build(),
                        TextFieldBuilder.create()
                                .marginInGridPane(new Insets(3))
                                .textFormatter(Formatters.forNonNegativeNumbers())
                                .textPropertyApply(prop -> prop
                                        .bindBidirectional(viewModel.weightProperty(), new NumberStringConverter()))
                                .build())
                .addRow(2,
                        LabelBuilder.create()
                                .text(I18n.get("main.bmi"))
                                .hAlignmentInGridPane(HPos.CENTER)
                                .build(),
                        LabelBuilder.create()
                                .text("0")
                                .marginInGridPane(new Insets(3))
                                .maxWidth(Double.MAX_VALUE)
                                .textPropertyApply(
                                        prop -> prop
                                                .bind(viewModel.bmiProperty()
                                                        .map(opt -> opt
                                                                .map(bmi -> String.format("%.1f", bmi))
                                                                .orElse("-"))))
                                .build())
                .addRow(3,
                        LabelBuilder.create()
                                .text(I18n.get("main.obesity"))
                                .hAlignmentInGridPane(HPos.CENTER)
                                .build(),
                        LabelBuilder.create()
                                .text("-")
                                .marginInGridPane(new Insets(3))
                                .maxWidth(Double.MAX_VALUE)
                                .textPropertyApply(prop -> prop
                                        .bind(viewModel.obesityProperty()
                                                .map(opt -> opt
                                                        .map(category -> I18n.get("main.obesity.category." + category))
                                                        .orElse("-"))))
                                .build())
                .add(
                        ButtonBuilder.create()
                                .text(I18n.get("main.record"))
                                .style("""
                                        -fx-corner-radius: 12px;
                                        """)
                                .hAlignmentInGridPane(HPos.CENTER)
                                .disablePropertyApply(prop -> prop.bind(viewModel.bmiProperty()
                                        .map(opt -> !opt.isPresent() || opt.isEmpty())))
                                .onAction(_ -> viewModel.saveBmiRecord())
                                .build(),
                        0, 4, 2, 1)
                .addColumnConstraints(
                        ColumnConstraintsBuilder.create()
                                .minWidth(70)
                                .build(),
                        ColumnConstraintsBuilder.create()
                                .hgrow(Priority.ALWAYS)
                                .build())
                .addRowConstraints(
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

    private void openSettingsWindow() {
        StageBuilder.create(StageStyle.DECORATED)
                .height(0)
                .width(0)
                .build()
                .show();
    }

}
