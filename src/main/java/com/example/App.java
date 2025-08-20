package com.example;

import java.util.Locale;

import com.example.model.repository.BmiRepositoryJooqImpl;
import com.example.model.repository.ConfigRepositoryPropertyImpl;
import com.example.model.service.BmiServiceImpl;
import com.example.model.service.ConfigServiceImpl;
import com.example.ui.utils.I18n;
import com.example.ui.view.CommonViewModel;
import com.example.ui.view.WindowManagerImpl;
import com.example.ui.view.main.MainView;
import com.example.ui.view.main.MainViewModel;
import com.example.ui.view.settings.SettingsView;

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
        I18n.getInstance().setResources("com.example.i18n.Messages", Locale.getDefault());

        var configService = new ConfigServiceImpl(new ConfigRepositoryPropertyImpl());

        var bmiService = new BmiServiceImpl(new BmiRepositoryJooqImpl());

        var windowManager = new WindowManagerImpl();

        var commonViewModel = new CommonViewModel(configService);

        windowManager.registerView(new SettingsView(commonViewModel));

        windowManager.registerView(new MainView(new MainViewModel(bmiService, windowManager, commonViewModel)));

        windowManager.showWindow(MainView.class, stage);
    }

}
