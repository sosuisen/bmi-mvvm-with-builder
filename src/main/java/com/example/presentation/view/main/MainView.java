package com.example.presentation.view.main;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import com.example.domain.model.BmiRecord;
import com.example.presentation.utils.Formatters;
import com.example.presentation.view.View;
import com.example.presentation.view.common.I18n;

import io.github.sosuisen.jfxbuilder.controls.ButtonBuilder;
import io.github.sosuisen.jfxbuilder.controls.CategoryAxisBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.controls.LineChartBuilder;
import io.github.sosuisen.jfxbuilder.controls.ListViewBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuBarBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuItemBuilder;
import io.github.sosuisen.jfxbuilder.controls.NumberAxisBuilder;
import io.github.sosuisen.jfxbuilder.controls.ScrollPaneBuilder;
import io.github.sosuisen.jfxbuilder.controls.TabBuilder;
import io.github.sosuisen.jfxbuilder.controls.TabPaneBuilder;
import io.github.sosuisen.jfxbuilder.controls.TextFieldBuilder;
import io.github.sosuisen.jfxbuilder.controls.XYChartSeriesBuilder;
import io.github.sosuisen.jfxbuilder.graphics.ColumnConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.GridPaneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.HBoxBuilder;
import io.github.sosuisen.jfxbuilder.graphics.RowConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class MainView implements View {
    private final String TITLE = "BMI Calc";
    private final Scene scene;
    private final MainViewModel viewModel;

    public MainView(MainViewModel viewModel) throws NullPointerException {
        this.viewModel = Objects.requireNonNull(viewModel);
        scene = buildSceneGraph();
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public Scene getScene() {
        return scene;
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
                                        HBoxBuilder
                                                .withChildren(
                                                        calculatorPanel(),
                                                        historyPanel())
                                                .build())
                                .build())
                .width(640)
                .height(480)
                .addStylesheetText(mainCSS)
                .build();
    }

    private MenuBar menuBar() {
        return MenuBarBuilder.create()
                .observableMenus(
                        MenuBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("menu.file")))
                                .observableItems(
                                        MenuItemBuilder.create()
                                                .textPropertyApply(
                                                        prop -> prop.bind(I18n.textProperty("menu.settings")))
                                                .onAction(_ -> viewModel.openSettingsWindow((Stage) scene.getWindow()))
                                                .build(),
                                        MenuItemBuilder.create()
                                                .textPropertyApply(
                                                        prop -> prop.bind(I18n.textProperty("menu.close")))
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
                .build();
    }

    private TabPane historyPanel() {
        return TabPaneBuilder.create()
                .observableTabs(
                        TabBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.list.tab")))
                                .content(historyListTab())
                                .closable(false)
                                .build(),
                        TabBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("history.chart.tab")))
                                .content(historyChartTab())
                                .closable(false)
                                .build())
                .maxWidth(Double.MAX_VALUE)
                .hGrowInHBox(Priority.ALWAYS)
                .build();
    }

    private VBox historyListTab() {
        return VBoxBuilder.create()
                .observableChildren(
                        LabelBuilder.create()
                                .textPropertyApply(
                                        prop -> prop.bind(I18n.textProperty("history.list.title")))
                                .build(),
                        ScrollPaneBuilder.create()
                                .fitToHeight(true)
                                .fitToWidth(true)
                                .content(
                                        ListViewBuilder.<BmiRecord>create()
                                                .items(viewModel.getBmiList())
                                                .cellFactory(this::recordsCellFactory)
                                                .build())
                                .vGrowInVBox(Priority.ALWAYS)
                                .build())
                .padding(new Insets(3))
                .build();

    }

    private ListCell<BmiRecord> recordsCellFactory(ListView<BmiRecord> listView) {
        return new ListCell<BmiRecord>() {
            @Override
            protected void updateItem(BmiRecord item, boolean empty) {
                super.updateItem(item, empty);

                textProperty().unbind();
                if (empty || item == null) {
                    setText(null);
                } else {
                    textProperty().bind(
                            I18n.textProperty("main.obesity.category." + item.obesity().toResourceString())
                                    .map(obesity -> String.format("[%s] %.1f (%s)",
                                            item.date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                            item.bmi(),
                                            obesity)));
                }
            }
        };
    }

    private LineChart<String, Number> historyChartTab() {
        var chartData = FXCollections.observableArrayList(new ArrayList<XYChart.Data<String, Number>>());
        chartData.addAll(viewModel.getBmiList().stream().map(this::bmiToChartData).toList());
        viewModel.getBmiList().addListener((ListChangeListener<BmiRecord>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    chartData.addAll(change.getAddedSubList().stream().map(this::bmiToChartData).toList());
                } else if (change.wasRemoved()) {
                    chartData.subList(change.getFrom(), change.getTo() + 1).clear();
                }
            }
        });

        return LineChartBuilder
                .<String, Number>create(
                        CategoryAxisBuilder.create()
                                .labelPropertyApply(
                                        prop -> prop.bind(I18n.textProperty("history.chart.xaxis")))
                                .build(),
                        NumberAxisBuilder.create()
                                .labelPropertyApply(
                                        prop -> prop.bind(I18n.textProperty("history.chart.yaxis")))
                                .build())
                .observableData(
                        XYChartSeriesBuilder.<String, Number>create()
                                .data(chartData)
                                .build())
                .title(I18n.text("history.chart.title"))
                .legendVisible(false)
                .prefWidth(300)
                .minWidth(200)
                .build();
    }

    private XYChart.Data<String, Number> bmiToChartData(BmiRecord bmiRecord) {
        return new XYChart.Data<>(bmiRecord.date().format(DateTimeFormatter.ofPattern("M/d")),
                bmiRecord.bmi());
    }

}
