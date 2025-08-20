package com.example.domain.service;

import com.example.domain.model.unit.Units;
import com.example.domain.exception.RepositoryException;

public interface ConfigService {
    Units getUnits() throws RepositoryException;

    void setUnits(Units units) throws RepositoryException;

    String getLanguage() throws RepositoryException;

    void setLanguage(String language) throws RepositoryException;

}