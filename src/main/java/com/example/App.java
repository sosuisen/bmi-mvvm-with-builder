package com.example;

import java.util.Locale;

import com.example.model.BmiService;
import com.example.ui.AlertDialog;
import com.example.ui.I18n;
import com.example.ui.View;
import com.example.ui.main.MainView;
import com.example.ui.main.MainViewModel;

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

        var service = new BmiService();

        var mainViewModel = new MainViewModel(service);

        var mainView = new MainView(mainViewModel);

        showMainWindow(stage, mainView);
    }

    /**
     * Shows the main window of the application.
     * 
     * @param stage the primary stage
     * @param model the application model
     */
    private void showMainWindow(Stage stage, View view) {
        try {

            stage.setScene(view.getScene());
            stage.setTitle(view.getTitle());
            stage.show();
        } catch (Exception e) {
            AlertDialog.showErrorAndExit("Failed to start the app", e);
        }
    }
}
