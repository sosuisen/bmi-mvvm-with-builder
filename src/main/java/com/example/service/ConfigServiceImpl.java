package com.example.service;

import com.example.domain.model.Languages;
import com.example.domain.model.unit.SIUnitsWithCentimeters;
import com.example.domain.model.unit.UnitSystem;
import com.example.domain.repository.ConfigRepository;

import com.example.domain.exception.RepositoryException;
import com.example.domain.service.ConfigService;
import com.example.presentation.utils.I18n;

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
    public UnitSystem getUnitSystem() throws RepositoryException {
        String unitType = configRepository.getConfig(UNIT_SYSTEM_KEY);
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
        throw new RepositoryException("No such unit system: " + unitType);
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
    public Languages getLanguage() throws RepositoryException {
        String langStr = configRepository.getConfig(LANGUAGE_KEY);

        if (langStr == null) {
            langStr = I18n.getInstance().getCurrentLocale().getLanguage();
        }

        var language = Languages.getLanguage(langStr);
        if (language == null) {
            return Languages.getDefaultLanguages();
        }

        return language;
    }

    @Override
    public void setLanguage(Languages language) throws RepositoryException {
        configRepository.setConfig(LANGUAGE_KEY, language.toLanguageString());
    }
}