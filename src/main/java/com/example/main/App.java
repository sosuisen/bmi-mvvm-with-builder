package com.example.main;

import java.util.Locale;

import com.example.presentation.utils.I18n;
import com.example.presentation.view.WindowManagerImpl;
import com.example.presentation.view.alert.AlertDialog;
import com.example.presentation.view.common.CommonViewModel;
import com.example.presentation.view.main.MainView;
import com.example.presentation.view.main.MainViewModel;
import com.example.presentation.view.settings.SettingsView;
import com.example.presentation.view.settings.SettingsViewModel;
import com.example.repository.BmiRepositoryJooqImpl;
import com.example.repository.ConfigRepositoryPropertyImpl;
import com.example.service.BmiServiceImpl;
import com.example.service.ConfigServiceImpl;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX MVC(Model-View-Controller) application
 */
public class App extends Application {
    /**
     * Called when the application is started.
     * 
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        var configService = new ConfigServiceImpl(new ConfigRepositoryPropertyImpl());

        try {
            I18n.getInstance().setResources(Locale.of(configService.getLanguage().toLanguageString()));
        } catch (Exception e) {
            AlertDialog.showErrorAndExit("Cannot load language", e);
        }

        var bmiService = new BmiServiceImpl(new BmiRepositoryJooqImpl());

        var windowManager = new WindowManagerImpl();

        var commonViewModel = new CommonViewModel(bmiService, configService);

        windowManager.registerView(new SettingsView(new SettingsViewModel(bmiService, commonViewModel)));

        windowManager.registerView(new MainView(new MainViewModel(bmiService, windowManager, commonViewModel)));

        windowManager.showWindow(MainView.class, stage);
    }

}
