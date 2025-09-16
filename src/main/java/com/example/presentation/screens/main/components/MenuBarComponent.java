package com.example.presentation.screens.main.components;

import com.example.presentation.helpers.I18n;
import com.example.presentation.screens.main.MainView;
import com.example.presentation.screens.main.MainViewModel;

import io.github.sosuisen.jfxbuilder.controls.MenuBarBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuItemBuilder;
import javafx.application.Platform;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

public class MenuBarComponent {
    public static MenuBar getRoot(MainViewModel viewModel, MainView mainView) {
        return MenuBarBuilder
            .withMenus(
                MenuBuilder
                    .withItems(
                        MenuItemBuilder.create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("menu.settings"))
                            )
                            .onAction(
                                event -> viewModel
                                    .openSettingsWindow((Stage) mainView.getScene().getWindow())
                            )
                            .build(),
                        MenuItemBuilder.create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("menu.close"))
                            )
                            .onAction(event -> Platform.exit())
                            .build()
                    )
                    .textPropertyApply(prop -> prop.bind(I18n.textProperty("menu.file")))
                    .build(),
                MenuBuilder
                    .withItems(
                        MenuItemBuilder.create()
                            .textPropertyApply(
                                prop -> prop.bind(I18n.textProperty("menu.about"))
                            )
                            .onAction(
                                event -> viewModel
                                    .openAboutWindow((Stage) mainView.getScene().getWindow())
                            )
                            .build()
                    )
                    .textPropertyApply(prop -> prop.bind(I18n.textProperty("menu.help")))
                    .build()
            )
            .build();
    }

}
