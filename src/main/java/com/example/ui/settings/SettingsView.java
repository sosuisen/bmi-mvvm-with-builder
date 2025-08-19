package com.example.ui.settings;

import java.time.format.DateTimeFormatter;

import com.example.model.domain.BmiRecord;
import com.example.ui.I18n;
import com.example.ui.Languages;
import com.example.ui.View;

import io.github.sosuisen.jfxbuilder.controls.ComboBoxBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.graphics.ColumnConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.GridPaneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.RowConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;

public class SettingsView implements View {
    private final String TITLE = "Settings";

    private final SettingsViewModel viewModel;
    private final Scene scene;

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public SettingsView(SettingsViewModel viewModel) {
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
        var rowConstraint = RowConstraintsBuilder.create()
                .vgrow(Priority.SOMETIMES)
                .minHeight(30)
                .build();

        return SceneBuilder
                .withRoot(
                        GridPaneBuilder.create()
                                .padding(new Insets(3))
                                .addRow(0,
                                        LabelBuilder.create()
                                                .text(I18n.get("main.height"))
                                                .hAlignmentInGridPane(HPos.CENTER)
                                                .build(),
                                        ComboBoxBuilder.create()
                                                .addItems(Languages.getValues())
                                                .build()

                                )
                                .addColumnConstraints(
                                        ColumnConstraintsBuilder.create()
                                                .minWidth(70)
                                                .build(),
                                        ColumnConstraintsBuilder.create()
                                                .hgrow(Priority.ALWAYS)
                                                .build())
                                .addRowConstraints(rowConstraint, rowConstraint)
                                .build())
                .width(240)
                .height(450)
                .addStylesheetText(mainCSS)
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
