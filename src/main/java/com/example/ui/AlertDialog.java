package com.example.ui;

import java.util.Objects;

import com.example.model.repository.RepositoryException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class AlertDialog {

    /**
     * Shows an error dialog based on the exception type.
     *
     * @param e the exception to display
     * @throws NullPointerException if e is null
     */
    public static void showError(Throwable e) throws NullPointerException {
        Objects.requireNonNull(e, "e must not be null");
        System.err.println(e.getMessage());

        var resourceName = switch (e) {
            case RepositoryException _ -> "error.repository";
            case NullPointerException _ -> "error.unexpected";
            case IllegalArgumentException _ -> "error.unexpected";
            case IllegalStateException _ -> "error.unexpected";
            default -> "error.unexpected";
        };
        var errStr = I18n.get(resourceName);

        Platform.runLater(() -> {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(I18n.get("error"));
            alert.setHeaderText(I18n.get("error"));
            alert.getDialogPane().setPrefWidth(480);
            alert.getDialogPane().setPrefHeight(240);
            // Resizable dialog
            alert.getDialogPane().setExpandableContent(new Label(errStr));
            alert.getDialogPane().setExpanded(true);
            alert.showAndWait();
        });
    }

    /**
     * Shows an error dialog with the given message and exception, then exits the
     * application.
     *
     * @param message the error message to display
     * @param e       the exception to display
     * @throws NullPointerException if message or e is null
     */
    public static void showErrorAndExit(String message, Exception e) throws NullPointerException {
        Objects.requireNonNull(message, "message must not be null");
        Objects.requireNonNull(e, "e must not be null");
        e.printStackTrace();

        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(I18n.get("error"));
        alert.setHeaderText(I18n.get("error"));
        alert.getDialogPane().setPrefWidth(320);
        alert.getDialogPane().setPrefHeight(160);
        // Make the dialog resizable
        alert.getDialogPane().setExpandableContent(new Label(message + ": " + e.getMessage()));
        alert.getDialogPane().setExpanded(true);
        alert.setOnHidden(_ -> Platform.exit());
        alert.show();
    }
}
