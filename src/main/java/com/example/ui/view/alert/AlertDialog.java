package com.example.ui.view.alert;

import java.util.Objects;

import com.example.model.repository.RepositoryException;
import com.example.ui.utils.I18n;

import io.github.sosuisen.jfxbuilder.controls.AlertBuilder;
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

        Platform.runLater(() -> {
            AlertBuilder.create(Alert.AlertType.ERROR)
                    .title(I18n.get("error"))
                    .headerText(I18n.get("error"))
                    .height(240)
                    .width(480)
                    .apply(alert -> {
                        alert.getDialogPane().setExpandableContent(new Label(I18n.get(resourceName)));
                        alert.getDialogPane().setExpanded(true);
                    })
                    .build()
                    .showAndWait();

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

        Platform.runLater(() -> {
            AlertBuilder.create(Alert.AlertType.ERROR)
                    .title(I18n.get("error"))
                    .headerText(I18n.get("error"))
                    .height(240)
                    .width(480)
                    .apply(alert -> {
                        alert.getDialogPane().setExpandableContent(new Label(message + ": " + e.getMessage()));
                        alert.getDialogPane().setExpanded(true);
                    })
                    .onHidden(_ -> Platform.exit())
                    .build()
                    .show();
        });

    }
}
