package com.example.presentation.view.settings;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.Languages;
import com.example.domain.model.unit.UnitSystem;
import com.example.presentation.view.View;
import com.example.presentation.view.alert.AlertDialog;
import com.example.presentation.view.common.GlobalCSS;
import com.example.presentation.view.common.I18n;

import io.github.sosuisen.jfxbuilder.controls.AlertBuilder;
import io.github.sosuisen.jfxbuilder.controls.ButtonBuilder;
import io.github.sosuisen.jfxbuilder.controls.ComboBoxBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.graphics.ColumnConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.GridPaneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.RowConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;

public class SettingsView implements View {
    public static final double WIDTH = 240;
    public static final double HEIGHT = 180;

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

    private static final String CSS = """
            .grid-pane {
                -fx-background-color: #f0f0f0;
                -fx-border-width: 1;
                -fx-border-color: black;
                -fx-border-style: solid;
            }
            """;

    private Scene buildSceneGraph() {
        var rowConstraint = RowConstraintsBuilder.create()
                .vgrow(Priority.SOMETIMES)
                .minHeight(30)
                .maxHeight(50)
                .build();

        return SceneBuilder
                .withRoot(
                        GridPaneBuilder.create()
                                .padding(new Insets(3))
                                .addRow(0,
                                        LabelBuilder.create()
                                                .textPropertyApply(
                                                        prop -> prop.bind(I18n.textProperty("settings.language")))
                                                .hAlignmentInGridPane(HPos.CENTER)
                                                .build(),
                                        ComboBoxBuilder.<Languages>create()
                                                .addItems(Languages.getLanguageList())
                                                .valuePropertyApply(prop -> prop
                                                        .bindBidirectional(viewModel.languageProperty()))
                                                .converterPropertyApply(prop -> prop.bind(Bindings
                                                        .createObjectBinding(() -> new LanguagesSystemConverter(),
                                                                I18n.INSTANCE.resourcesProperty())))
                                                .build())
                                .addRow(1,
                                        LabelBuilder.create()
                                                .textPropertyApply(
                                                        prop -> prop.bind(I18n.textProperty("settings.unitsystem")))
                                                .hAlignmentInGridPane(HPos.CENTER)
                                                .build(),
                                        ComboBoxBuilder.<UnitSystem>create()
                                                .addItems(UnitSystem.getAll())
                                                .valuePropertyApply(prop -> prop
                                                        .bindBidirectional(viewModel.unitSystemProperty()))
                                                .converterPropertyApply(prop -> prop.bind(Bindings
                                                        .createObjectBinding(() -> new UnitSystemStringConverter(),
                                                                I18n.INSTANCE.resourcesProperty())))
                                                .build())
                                .addRow(2,
                                        LabelBuilder.create()
                                                .textPropertyApply(prop -> prop
                                                        .bind(I18n.textProperty("settings.clearrecords.label")))
                                                .hAlignmentInGridPane(HPos.CENTER)
                                                .build(),
                                        ButtonBuilder.create()
                                                .id("clear-button")
                                                .textPropertyApply(prop -> prop
                                                        .bind(I18n.textProperty("settings.clearrecords.button")))
                                                .style("""
                                                        -fx-corner-radius: 12px;
                                                        """)
                                                .addStyleClass("button-danger")
                                                .hAlignmentInGridPane(HPos.CENTER)
                                                .onAction(_ -> removeAllRecords())
                                                .build())
                                .addColumnConstraints(
                                        ColumnConstraintsBuilder.create()
                                                .minWidth(120)
                                                .build(),
                                        ColumnConstraintsBuilder.create()
                                                .hgrow(Priority.ALWAYS)
                                                .build())
                                .addRowConstraints(rowConstraint, rowConstraint, rowConstraint)
                                .build())
                .width(WIDTH)
                .height(HEIGHT)
                .addStylesheetsText(CSS)
                .addStylesheetsText(GlobalCSS.CSS)
                .build();
    }

    private void removeAllRecords() {
        AlertBuilder.create(Alert.AlertType.CONFIRMATION)
                .title(I18n.text("settings.clearrecords.label"))
                .headerText(I18n.text("settings.clearrecords.confirm"))
                .apply(alert -> {
                    var okBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                    okBtn.setDefaultButton(false);
                    var cancelBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                    cancelBtn.setDefaultButton(true);
                })
                .build()
                .showAndWait()
                .filter(buttonType -> buttonType == ButtonType.OK)
                .ifPresent(_ -> {
                    try {
                        viewModel.removeAllRecords();
                    } catch (RepositoryException e) {
                        AlertDialog.showError(e);
                    }
                });
    }

    class LanguagesSystemConverter extends StringConverter<Languages> {
        @Override
        public String toString(Languages language) {
            return I18n.text(
                    "language." + language
                            .toLanguageString());
        }

        @Override
        public Languages fromString(String string) {
            return null;
        }
    }

    class UnitSystemStringConverter extends StringConverter<UnitSystem> {
        @Override
        public String toString(UnitSystem unitSystem) {
            return I18n.text(
                    "unitsystem." + unitSystem
                            .toResourceString());
        }

        @Override
        public UnitSystem fromString(String string) {
            return null;
        }
    }

}
