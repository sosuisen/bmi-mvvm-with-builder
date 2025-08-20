package com.example.model.repository;

public interface ConfigRepository {
    /**
     * Get the serialized config value by key.
     * If the key does not exist, it returns null.
     * 
     * @param key
     * @throws RepositoryException
     */
    String getConfig(String key) throws RepositoryException;

    void setConfig(String key, String value) throws RepositoryException;
}