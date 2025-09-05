package com.example.presentation;

import com.example.presentation.screens.View;

import javafx.stage.Stage;

public interface WindowManager {
    void registerView(View view) throws NullPointerException;

    <T extends View> void showWindow(Class<T> viewClass, Stage stage)
        throws NullPointerException;
}
