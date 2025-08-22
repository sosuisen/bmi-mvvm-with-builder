package com.example.presentation.view.main.components;

import com.example.presentation.view.common.I18n;
import com.example.presentation.view.main.MainView;
import com.example.presentation.view.main.MainViewModel;

import io.github.sosuisen.jfxbuilder.controls.MenuBarBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuBuilder;
import io.github.sosuisen.jfxbuilder.controls.MenuItemBuilder;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

public class MenuBarComponent {
    public static MenuBar getRoot(MainViewModel viewModel, MainView mainView) {
        return MenuBarBuilder.create()
                .observableMenus(
                        MenuBuilder.create()
                                .textPropertyApply(prop -> prop.bind(I18n.textProperty("menu.file")))
                                .observableItems(
                                        MenuItemBuilder.create()
                                                .textPropertyApply(
                                                        prop -> prop.bind(I18n.textProperty("menu.settings")))
                                                .onAction(_ -> viewModel
                                                        .openSettingsWindow((Stage) mainView.getScene().getWindow()))
                                                .build(),
                                        MenuItemBuilder.create()
                                                .textPropertyApply(
                                                        prop -> prop.bind(I18n.textProperty("menu.close")))
                                                .onAction(_ -> Platform.exit())
                                                .build())
                                .build())
                .build();
    }

}
