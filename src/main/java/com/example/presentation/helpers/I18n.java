package com.example.presentation.helpers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum I18n {
    INSTANCE;

    private static final String BASE_NAME = "com.example.i18n.Messages";

    private final ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

    private final Map<String, StringProperty> stringProperties = new HashMap<>();

    public static I18n getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the resource bundle with the given base name and locale.
     *
     * @param locale the locale
     * @throws NullPointerException if baseName or locale is null
     * @throws IllegalArgumentException if baseName is empty
     */
    public void setResources(Locale locale)
        throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(locale, "locale must not be null");
        // When locale is "fr",
        // 1) search Messages_fr.properties
        // 2) search OS default locale, e.g. Messages_en.properties
        // 3) search Messages.properties
        resources.set(ResourceBundle.getBundle(BASE_NAME, locale));
    }

    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }

    public static StringProperty textProperty(String key) throws NullPointerException {
        Objects.requireNonNull(key, "key must not be null");
        return INSTANCE.getStringProperty(key);
    }

    public static String text(String key) throws NullPointerException {
        Objects.requireNonNull(key, "key must not be null");
        return INSTANCE.getString(key);
    }

    private StringProperty getStringProperty(String key) {
        return stringProperties.computeIfAbsent(key, k -> {
            var prop = new SimpleStringProperty();
            resources.addListener((obs, oldValue, newValue) -> prop.set(getString(key)));
            prop.set(getString(key));
            return prop;
        });
    }

    private String getString(String key) throws NullPointerException {
        Objects.requireNonNull(key, "key must not be null");
        try {
            return resources.get().getString(key);
        } catch (Exception e) {
            System.err.println("I18n: Missing resource key : " + key);
            return key;
        }
    }
}
