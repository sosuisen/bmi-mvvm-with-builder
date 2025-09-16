package com.example.presentation.screens.about;

import com.example.presentation.screens.View;

import io.github.sosuisen.jfxbuilder.controls.ButtonBuilder;
import io.github.sosuisen.jfxbuilder.controls.HyperlinkBuilder;
import io.github.sosuisen.jfxbuilder.controls.LabelBuilder;
import io.github.sosuisen.jfxbuilder.graphics.SceneBuilder;
import io.github.sosuisen.jfxbuilder.graphics.VBoxBuilder;
import javafx.application.HostServices;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class AboutView implements View {
    public static final double WIDTH = 240;
    public static final double HEIGHT = 100;

    private final String TITLE = "About";

    private final Scene scene;

    private final HostServices hostService;

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public AboutView(HostServices hostService) {
        this.hostService = hostService;
        scene = buildSceneGraph();
    }

    private Scene buildSceneGraph() {
        var url = "https://github.com/sosuisen/";
        return SceneBuilder
            .withRoot(
                VBoxBuilder
                    .withChildren(
                        LabelBuilder.create()
                            .text("BMI Calculator")
                            .style("-fx-font-weight: bold;")
                            .build(),
                        LabelBuilder.create()
                            .text("Created by Hidekazu Kubota")
                            .build(),
                        HyperlinkBuilder.create()
                            .text(url)
                            .onAction(event -> hostService.showDocument(url))
                            .build(),
                        ButtonBuilder.create()
                            .text("OK")
                            .onAction(event -> close())
                            .build()
                    )
                    .alignment(Pos.CENTER)
                    .build()
            )
            .width(WIDTH)
            .height(HEIGHT)
            .onKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE)
                    close();
            })
            .build();
    }

    private void close() {
        ((Stage) scene.getWindow()).close();
    }

}
