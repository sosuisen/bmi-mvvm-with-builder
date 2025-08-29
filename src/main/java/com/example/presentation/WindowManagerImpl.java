package com.example.presentation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.example.presentation.screens.View;
import com.example.presentation.screens.alert.AlertDialog;

import javafx.stage.Stage;

public class WindowManagerImpl implements WindowManager {
    private final Map<Class<? extends View>, View> views = new HashMap<>();

    @Override
    public void registerView(View view) throws NullPointerException {
        Objects.requireNonNull(view);
        views.put(view.getClass(), view);
    }

    @Override
    public <T extends View> void showWindow(Class<T> viewClass, Stage stage) throws NullPointerException {
        Objects.requireNonNull(stage);
        var view = views.get(Objects.requireNonNull(viewClass));
        if (view != null) {
            if (!stage.isShowing()) {
                try {
                    stage.setScene(view.getScene());
                    stage.setTitle(view.getTitle());
                    stage.show();
                } catch (Exception e) {
                    AlertDialog.showErrorAndExit("Failed to start the app", e);
                }
            }
        } else {
            throw new IllegalStateException("Unregistered view class: " + viewClass.getName());
        }
    }
}