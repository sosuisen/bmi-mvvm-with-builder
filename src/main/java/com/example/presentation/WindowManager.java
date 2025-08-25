package com.example.presentation;

import com.example.presentation.screens.View;

import javafx.stage.Stage;

public interface WindowManager {
    public void registerView(View view) throws NullPointerException;

    public <T extends View> void showWindow(Class<T> viewClass, Stage stage)
            throws NullPointerException;
}
