package com.example.domain.model;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the supported languages in the application.
 * Each enum constant corresponds to a language and its string representation
 * is used for resource keys or locales(e.g., "en", "ja").
 *
 * <p>
 * The resouce bundles corresponding to the Languages should exist.
 * </p>
 */
public enum Languages {
    EN,
    JA;

    /**
     * Returns a list of all available language enum constants.
     *
     * @return A list of {@link Languages} enum constants.
     */
    public static List<Languages> getLanguageList() {
        return Arrays.asList(Languages.values());
    }

    /**
     * Converts a string value to its corresponding {@link Languages} enum constant.
     *
     * @param value The string representation of the language (e.g., "en", "ja").
     * @return The {@link Languages} enum constant.
     * @throws IllegalArgumentException if the provided string does not match any
     *                                  language constant.
     */
    public static Languages getLanguage(String value) {
        return Languages.valueOf(value.toUpperCase());
    }

    /**
     * Returns the default language
     *
     * @return The default {@link Languages} enum constant.
     */
    public static Languages getDefaultLanguages() {
        return EN;
    }

    /**
     * Converts the enum constant to its lowercase string representation, suitable
     * for language codes.
     *
     * @return The lowercase string representation of the language (e.g., "en",
     *         "ja").
     */
    public String toLanguageString() {
        return toString().toLowerCase();
    }
}
