package com.example.repository;

public class AppDirectory {
    private static final String APP_DIR = ".bmi-app";

    public static String getAppDirPath() {
        return System.getProperty("user.home") + "/" + APP_DIR;
    }
}
