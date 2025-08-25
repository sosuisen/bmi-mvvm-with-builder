package com.example.domain.repository;

import com.example.domain.exception.RepositoryException;

public interface ConfigRepository {

    /**
     * Retrieves a configuration value by its key.
     *
     * @param key The key of the configuration item.
     * @return The value associated with the key, or null if not found.
     * @throws RepositoryException If an error occurs during the retrieval process.
     */
    String getConfig(String key) throws RepositoryException;

    /**
     * Sets a configuration value for a given key.
     *
     * @param key   The key of the configuration item.
     * @param value The value to set.
     * @throws RepositoryException If an error occurs during the setting process.
     */
    void setConfig(String key, String value) throws RepositoryException;
}