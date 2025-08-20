package com.example.presentation.view.settings;

import com.example.domain.model.Languages;
import com.example.domain.model.unit.UnitSystem;
import com.example.presentation.view.View;
import com.example.presentation.view.common.CommonViewModel;
import com.example.presentation.view.common.I18n;

import io.github.sosuisen.jfxbuilder.controls.ComboBoxBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.graphics.ColumnConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.GridPaneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.RowConstraintsBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;

public class SettingsView implements View {
    private final String TITLE = "Settings";

    private final CommonViewModel commonViewModel;
    private final Scene scene;

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public SettingsView(CommonViewModel commonViewModel) {
        this.commonViewModel = commonViewModel;
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
                .maxHeight(50)
                .build();

        return SceneBuilder
                .withRoot(
                        GridPaneBuilder.create()
                                .padding(new Insets(3))
                                .addRow(0,
                                        LabelBuilder.create()
                                                .text(I18n.get("settings.language"))
                                                .hAlignmentInGridPane(HPos.CENTER)
                                                .build(),
                                        ComboBoxBuilder.<Languages>create()
                                                .observableItems(Languages.getLanguageList())
                                                .valuePropertyApply(prop -> prop
                                                        .bindBidirectional(commonViewModel.languageProperty()))
                                                .converter(new StringConverter<Languages>() {
                                                    @Override
                                                    public String toString(Languages language) {
                                                        return I18n
                                                                .get("language." + language.toLanguageString());
                                                    }

                                                    @Override
                                                    public Languages fromString(String string) {
                                                        return null;
                                                    }
                                                })
                                                .build()

                                )
                                .addRow(1,
                                        LabelBuilder.create()
                                                .text(I18n.get("settings.unitsystem"))
                                                .hAlignmentInGridPane(HPos.CENTER)
                                                .build(),
                                        ComboBoxBuilder.<UnitSystem>create()
                                                .observableItems(UnitSystem.getAll())
                                                .valuePropertyApply(prop -> prop
                                                        .bindBidirectional(commonViewModel.unitSystemProperty()))
                                                .converter(new StringConverter<UnitSystem>() {
                                                    @Override
                                                    public String toString(UnitSystem unitSystem) {
                                                        return I18n
                                                                .get("unitsystem."
                                                                        + unitSystem.toResourceString());
                                                    }

                                                    @Override
                                                    public UnitSystem fromString(String string) {
                                                        return null;
                                                    }
                                                })
                                                .build())
                                .observableColumnConstraints(
                                        ColumnConstraintsBuilder.create()
                                                .minWidth(70)
                                                .build(),
                                        ColumnConstraintsBuilder.create()
                                                .hgrow(Priority.ALWAYS)
                                                .build())
                                .observableRowConstraints(rowConstraint, rowConstraint)
                                .build())
                .width(240)
                .height(350)
                .addStylesheetText(mainCSS)
                .build();
    }

}
