package com.example.service;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.Languages;
import com.example.domain.model.unit.SIUnitsWithCentimeters;
import com.example.domain.model.unit.UnitSystem;
import com.example.domain.repository.ConfigRepository;

import java.util.Locale;

import com.example.domain.service.ConfigService;

public class ConfigServiceImpl implements ConfigService {
    private static final String UNIT_SYSTEM_KEY = "unit.system";
    private static final String LANGUAGE_KEY = "language";

    private final ConfigRepository configRepository;

    public ConfigServiceImpl(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    /**
     * Get the selected unit system from the repository.
     * If the repository has no unit system, use SI.
     * If the app cannot handle the selected unit system,
     * return SI.
     * 
     * @throws RepositoryException
     */
    @Override
    public UnitSystem getUnitSystem() {
        String unitType;
        try {
            unitType = configRepository.getConfig(UNIT_SYSTEM_KEY);
        } catch (RepositoryException e) {
            unitType = null;
        }

        if (unitType == null) {
            return new SIUnitsWithCentimeters();
        }

        for (var unitSystem : UnitSystem.class.getPermittedSubclasses()) {
            if (unitSystem.getSimpleName().equals(unitType)) {
                try {
                    return (UnitSystem) unitSystem.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    return new SIUnitsWithCentimeters();
                }
            }
        }
        return new SIUnitsWithCentimeters();
    }

    @Override
    public void setUnitSystem(UnitSystem unitSystem) throws RepositoryException {
        configRepository.setConfig(UNIT_SYSTEM_KEY, unitSystem.getClass().getSimpleName());
    }

    /**
     * Get the selected language from the repository.
     * If the repository has no language, use the default OS language.
     * If the app cannot handle the selected language, return the app's default
     * language.
     * 
     * @throws RepositoryException
     */
    @Override
    public Languages getLanguage() {
        String langStr;
        try {
            langStr = configRepository.getConfig(LANGUAGE_KEY);
        } catch (RepositoryException e) {
            langStr = null;
        }

        if (langStr == null) {
            langStr = Locale.getDefault().getLanguage();
        }

        try {
            return Languages.getLanguage(langStr);
        } catch (IllegalArgumentException e) {
            return Languages.getDefaultLanguages();
        }
    }

    @Override
    public void setLanguage(Languages language) throws RepositoryException {
        configRepository.setConfig(LANGUAGE_KEY, language.toLanguageString());
    }
}