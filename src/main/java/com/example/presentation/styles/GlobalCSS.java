package com.example.presentation.styles;

public class GlobalCSS {
    public static final String CSS =
        """
        .button-small {
            -fx-font-size: 10;
        }

        .button-safe {
            -fx-background-color: #006000;
            -fx-text-fill: white;
        }

        .button-safe:hover {
            -fx-background-color: #009000;
            transition: -fx-background-color 0.3s;
            -fx-cursor: hand;
        }

        .button-danger {
            -fx-background-color: #900000;
            -fx-text-fill: white;
            -fx-alignment: center;
        }

        .button-danger:hover {
            -fx-background-color: #c05050;
            transition: -fx-background-color 0.3s;
            -fx-cursor: hand;
        }

        .label {
            -fx-alignment: center;
        }

        .label-bold {
            -fx-font-weight: bold;
        }
        """;
}
