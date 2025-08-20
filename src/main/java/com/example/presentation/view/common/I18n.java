package com.example.presentation.view.common;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public enum I18n {
    INSTANCE;

    private static final String BASE_NAME = "com.example.i18n.Messages";

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
    public void setResources(Locale locale)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(locale, "locale must not be null");
        // When locale is "fr",
        // 1) search Messages_fr.properties
        // 2) search OS default locale, e.g. Messages_en.properties
        // 3) search Messages.properties
        resources = ResourceBundle.getBundle(BASE_NAME, locale);
    }

    public Locale getCurrentLocale() {
        return INSTANCE.resources.getLocale();
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
        try {
            return resources != null ? resources.getString(key) : key;
        } catch (Exception e) {
            System.err.println("I18n: Missing resource key : " + key);
            return key;
        }
    }
}
