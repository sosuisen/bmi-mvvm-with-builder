package com.example.presentation.screens.main;

import java.util.Objects;

import com.example.presentation.screens.View;
import com.example.presentation.screens.alert.AlertDialog;
import com.example.presentation.screens.main.components.CalculatorComponent;
import com.example.presentation.screens.main.components.HistoryComponent;
import com.example.presentation.screens.main.components.MenuBarComponent;
import com.example.presentation.styles.GlobalCSS;

import io.github.sosuisen.jfxbuilder.graphics.HBoxBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;

public class MainView implements View {
    private final String TITLE = "BMI Calculator";
    private final Scene scene;
    private final MainViewModel viewModel;

    public MainView(MainViewModel viewModel) throws NullPointerException {
        this.viewModel = Objects.requireNonNull(viewModel);
        scene = buildSceneGraph();

        viewModel.errorProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                AlertDialog.showError(newValue);
                viewModel.errorProperty().set(null);
            }
        });
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    private Scene buildSceneGraph() {
        return SceneBuilder
            .withRoot(
                VBoxBuilder
                    .withChildren(
                        MenuBarComponent.getRoot(viewModel, this),
                        HBoxBuilder
                            .withChildren(
                                CalculatorComponent.getRoot(viewModel),
                                HistoryComponent.getRoot(viewModel)
                            )
                            .vGrowInVBox(Priority.ALWAYS)
                            .build()
                    )
                    .build()
            )
            .width(660)
            .height(480)
            .addStylesheetsText(GlobalCSS.CSS)
            .build();
    }
}
