package com.example.model.repository;

public interface ConfigRepository {
    String getProperty(String key) throws RepositoryException;

    void setProperty(String key, String value) throws RepositoryException;
}