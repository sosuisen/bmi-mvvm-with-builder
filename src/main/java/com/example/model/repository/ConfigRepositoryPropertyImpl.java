package com.example.model.repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigRepositoryPropertyImpl implements ConfigRepository {
    private static final String CONFIG_DIR = ".bmi-app";
    private static final String CONFIG_FILE = "config.properties";
    private final Properties properties;
    private final Path configPath;

    public ConfigRepositoryPropertyImpl() {
        this.properties = new Properties();
        this.configPath = Paths.get(System.getProperty("user.home"), CONFIG_DIR, CONFIG_FILE);
    }

    @Override
    public String getConfig(String key) throws RepositoryException {
        load();
        return properties.getProperty(key);
    }

    @Override
    public void setConfig(String key, String value) throws RepositoryException {
        properties.setProperty(key, value);
        save();
    }

    private void save() throws RepositoryException {
        try {
            configPath.getParent().toFile().mkdirs();
            try (FileOutputStream out = new FileOutputStream(configPath.toFile())) {
                properties.store(out, "BMI Application Configuration");
            }
        } catch (IOException e) {
            throw new RepositoryException("Failed to save configuration", e);
        }
    }

    private void load() throws RepositoryException {
        if (configPath.toFile().exists()) {
            try (FileInputStream in = new FileInputStream(configPath.toFile())) {
                properties.load(in);
            } catch (IOException e) {
                throw new RepositoryException("Failed to load configuration", e);
            }
        }
    }
}