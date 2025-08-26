package com.example.domain.service;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.Languages;
import com.example.domain.model.unit.UnitSystem;

/**
 * Service interface for managing application configuration, such as unit system
 * and language.
 */
public interface ConfigService {
    /**
     * Retrieves the configured unit system.
     *
     * @return The configured {@link UnitSystem}.
     */
    UnitSystem getUnitSystem();

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
     */
    Languages getLanguage();

    /**
     * Sets the language in the configuration.
     *
     * @param language The {@link Languages} to set.
     * @throws RepositoryException If an error occurs during setting.
     */
    void setLanguage(Languages language) throws RepositoryException;

}