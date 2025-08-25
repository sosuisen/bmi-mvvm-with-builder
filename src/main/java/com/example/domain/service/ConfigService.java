package com.example.domain.service;

import com.example.domain.model.Languages;
import com.example.domain.model.unit.UnitSystem;
import com.example.domain.exception.RepositoryException;

/**
 * Service interface for managing application configuration, such as unit system
 * and language.
 */
public interface ConfigService {
    /**
     * Retrieves the configured unit system.
     *
     * @return The configured {@link UnitSystem}.
     * @throws RepositoryException If an error occurs during retrieval.
     */
    UnitSystem getUnitSystem() throws RepositoryException;

    /**
     * Sets the unit system in the configuration.
     *
     * @param unitSystem The {@link UnitSystem} to set.
     * @throws RepositoryException If an error occurs during setting.
     */
    void setUnitSystem(UnitSystem unitSystem) throws RepositoryException;

    /**
     * Retrieves the configured language.
     *
     * @return The configured {@link Languages}.
     * @throws RepositoryException If an error occurs during retrieval.
     */
    Languages getLanguage() throws RepositoryException;

    /**
     * Sets the language in the configuration.
     *
     * @param language The {@link Languages} to set.
     * @throws RepositoryException If an error occurs during setting.
     */
    void setLanguage(Languages language) throws RepositoryException;

}