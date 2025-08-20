package com.example;

import java.util.Locale;

import com.example.model.BmiService;
import com.example.model.ConfigService;
import com.example.model.ConfigServiceImpl;
import com.example.model.domain.unit.SIUnitsWithCentimeters;
import com.example.model.repository.BmiRepositoryJooqImpl;
import com.example.model.repository.ConfigRepository;
import com.example.model.repository.ConfigRepositoryPropertyImpl;
import com.example.model.repository.RepositoryException;
import com.example.ui.utils.I18n;
import com.example.ui.view.WindowManagerImpl;
import com.example.ui.view.common.AlertDialog;
import com.example.ui.view.main.MainView;
import com.example.ui.view.main.MainViewModel;
import com.example.ui.view.settings.SettingsView;
import com.example.ui.view.settings.SettingsViewModel;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX MVC(Model-View-Controller) application
 */
public class App extends Application {
    private ConfigService configService;

    /**
     * Called when the application is started.
     * 
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        I18n.getInstance().setResources("com.example.i18n.Messages", Locale.getDefault());
        try {
            configService = new ConfigServiceImpl(new ConfigRepositoryPropertyImpl());
            configService.loadConfig();
        } catch (RepositoryException e) {
            AlertDialog.showErrorAndExit("Cannot load config", e);
        }

        var bmiService = new BmiService(new BmiRepositoryJooqImpl());

        var windowManager = new WindowManagerImpl();

        var defaultUnits = new SIUnitsWithCentimeters();

        windowManager.registerView(new SettingsView(new SettingsViewModel()));

        windowManager.registerView(new MainView(new MainViewModel(bmiService, windowManager, defaultUnits)));

        windowManager.showWindow(MainView.class, stage);
    }

    @Override
    public void stop() throws Exception {
        configService.saveConfig();
        super.stop();
    }

}
