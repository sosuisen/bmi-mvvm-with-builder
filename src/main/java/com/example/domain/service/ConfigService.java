package com.example.domain.service;

import com.example.domain.model.Languages;
import com.example.domain.model.unit.UnitSystem;
import com.example.domain.exception.RepositoryException;

public interface ConfigService {
    UnitSystem getUnitSystem() throws RepositoryException;

    void setUnitSystem(UnitSystem unitSystem) throws RepositoryException;

    Languages getLanguage() throws RepositoryException;

    void setLanguage(Languages language) throws RepositoryException;

}