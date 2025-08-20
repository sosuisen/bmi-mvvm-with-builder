package com.example.main;

import java.util.Locale;

import com.example.presentation.view.WindowManagerImpl;
import com.example.presentation.view.common.CommonViewModel;
import com.example.presentation.view.common.I18n;
import com.example.presentation.view.main.MainView;
import com.example.presentation.view.main.MainViewModel;
import com.example.presentation.view.settings.SettingsView;
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
        I18n.getInstance().setResources(Locale.getDefault());

        var configService = new ConfigServiceImpl(new ConfigRepositoryPropertyImpl());

        var bmiService = new BmiServiceImpl(new BmiRepositoryJooqImpl());

        var windowManager = new WindowManagerImpl();

        var commonViewModel = new CommonViewModel(configService);

        windowManager.registerView(new SettingsView(commonViewModel));

        windowManager.registerView(new MainView(new MainViewModel(bmiService, windowManager, commonViewModel)));

        windowManager.showWindow(MainView.class, stage);
    }

}
