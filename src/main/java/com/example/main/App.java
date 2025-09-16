package com.example.main;

import java.util.Locale;

import com.example.domain.exception.RepositoryException;
import com.example.domain.service.BmiService;
import com.example.presentation.WindowManagerImpl;
import com.example.presentation.appmodel.BmiCommonAppModel;
import com.example.presentation.appmodel.ConfigAppModel;
import com.example.presentation.helpers.I18n;
import com.example.presentation.screens.about.AboutView;
import com.example.presentation.screens.alert.AlertDialog;
import com.example.presentation.screens.main.MainView;
import com.example.presentation.screens.main.MainViewModel;
import com.example.presentation.screens.settings.SettingsView;
import com.example.presentation.screens.settings.SettingsViewModel;
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
        /*
         * Services will be called from application models
         */
        var configService = new ConfigServiceImpl(new ConfigRepositoryPropertyImpl());

        try {
            I18n.getInstance()
                .setResources(Locale.forLanguageTag(configService.getLanguage().toLanguageString()));
        } catch (Exception e) {
            AlertDialog.showErrorAndExit("Cannot load language", e);
            return;
        }

        BmiService bmiService;
        try {
            bmiService = new BmiServiceImpl(new BmiRepositoryJooqImpl());
        } catch (RepositoryException e) {
            AlertDialog.showErrorAndExit("Cannot load BMI records", e);
            return;
        }

        /*
         * WindowsManager manages views.
         */
        var windowManager = new WindowManagerImpl();

        /*
         * Application models are shared among multiple views.
         */
        var bmiCommonAppModel = new BmiCommonAppModel(bmiService);
        var configAppModel = new ConfigAppModel(configService, bmiCommonAppModel);

        /*
         * Register each view along with its own view model to the WindowManager.
         */
        windowManager.registerView(
            new SettingsView(new SettingsViewModel(bmiCommonAppModel, configAppModel))
        );

        windowManager.registerView(
            new MainView(new MainViewModel(windowManager, bmiCommonAppModel, configAppModel))
        );

        windowManager.registerView(new AboutView(getHostServices()));

        windowManager.showWindow(MainView.class, stage);
    }

}
