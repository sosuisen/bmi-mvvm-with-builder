package com.example.model.repository;

public interface ConfigRepository {
    String getProperty(String key);
    
    void setProperty(String key, String value) throws RepositoryException;
    
    void save() throws RepositoryException;
    
    void load() throws RepositoryException;
}