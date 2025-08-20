package com.example.model;

import com.example.model.domain.unit.Units;
import com.example.model.repository.RepositoryException;

public interface ConfigService {
    Units getUnits() throws RepositoryException;
    
    void setUnits(Units units) throws RepositoryException;
    
    String getLanguage() throws RepositoryException;
    
    void setLanguage(String language) throws RepositoryException;
    
    void saveConfig() throws RepositoryException;
    
    void loadConfig() throws RepositoryException;
}