package com.example.ui;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public enum I18n {
    INSTANCE;

    private ResourceBundle resources;

    public static I18n getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the resource bundle with the given base name and locale.
     *
     * @param baseName the base name of the resource bundle
     * @param locale   the locale
     * @throws NullPointerException     if baseName or locale is null
     * @throws IllegalArgumentException if baseName is empty
     */
    public void setResources(String baseName, Locale locale)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(baseName, "baseName must not be null");
        Objects.requireNonNull(locale, "locale must not be null");
        if (baseName.isBlank()) {
            throw new IllegalArgumentException("baseName must not be empty");
        }
        this.resources = ResourceBundle.getBundle(baseName, locale);
    }

    /**
     * Retrieves the resource string corresponding to the given key.
     * 
     * @param key the resource key
     * @return the resource string, or the key itself if no resource is set
     * @throws NullPointerException if key is null
     */
    public static String get(String key) throws NullPointerException {
        return INSTANCE.getString(key);
    }

    private String getString(String key) throws NullPointerException {
        Objects.requireNonNull(key, "key must not be null");
        return resources != null ? resources.getString(key) : key;
    }
}
