package com.example.model.repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigRepositoryPropertyImpl implements ConfigRepository {
    private static final String CONFIG_FILE = "config.properties";
    private final Properties properties;
    private final Path configPath;

    public ConfigRepositoryPropertyImpl() {
        this.properties = new Properties();
        this.configPath = Paths.get(System.getProperty("user.home"), ".bmi-app", CONFIG_FILE);
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public void setProperty(String key, String value) throws RepositoryException {
        properties.setProperty(key, value);
    }

    @Override
    public void save() throws RepositoryException {
        try {
            configPath.getParent().toFile().mkdirs();
            try (FileOutputStream out = new FileOutputStream(configPath.toFile())) {
                properties.store(out, "BMI Application Configuration");
            }
        } catch (IOException e) {
            throw new RepositoryException("Failed to save configuration", e);
        }
    }

    @Override
    public void load() throws RepositoryException {
        if (configPath.toFile().exists()) {
            try (FileInputStream in = new FileInputStream(configPath.toFile())) {
                properties.load(in);
            } catch (IOException e) {
                throw new RepositoryException("Failed to load configuration", e);
            }
        }
    }
}