package com.example.presentation.view.main;

import java.util.Objects;

import com.example.presentation.view.View;
import com.example.presentation.view.alert.AlertDialog;
import com.example.presentation.view.common.GlobalCSS;
import com.example.presentation.view.main.components.CalculatorComponent;
import com.example.presentation.view.main.components.HistoryComponent;
import com.example.presentation.view.main.components.MenuBarComponent;

import io.github.sosuisen.jfxbuilder.graphics.HBoxBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;

public class MainView implements View {
    private final String TITLE = "BMI Calc";
    private final Scene scene;
    private final MainViewModel viewModel;

    public MainView(MainViewModel viewModel) throws NullPointerException {
        this.viewModel = Objects.requireNonNull(viewModel);
        scene = buildSceneGraph();

        viewModel.errorProperty().subscribe(err -> {
            if (err != null) {
                AlertDialog.showError(err);
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
                                                        HistoryComponent.getRoot(viewModel))
                                                .vGrowInVBox(Priority.ALWAYS)
                                                .build())
                                .build())
                .width(640)
                .height(480)
                .addStylesheetsText(GlobalCSS.CSS)
                .build();
    }
}
