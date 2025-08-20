package com.example.model.service;

import com.example.model.domain.unit.ImperialUnits;
import com.example.model.domain.unit.SIUnitsWithCentimeters;
import com.example.model.domain.unit.Units;
import com.example.model.repository.ConfigRepository;
import com.example.model.repository.RepositoryException;

public class ConfigServiceImpl implements ConfigService {
    private static final String UNIT_SYSTEM_KEY = "unit.system";
    private static final String LANGUAGE_KEY = "language";
    private static final String SI_UNITS = "SI";
    private static final String IMPERIAL_UNITS = "IMPERIAL";

    private final ConfigRepository configRepository;

    public ConfigServiceImpl(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public Units getUnits() throws RepositoryException {
        String unitType = configRepository.getProperty(UNIT_SYSTEM_KEY);
        if (IMPERIAL_UNITS.equals(unitType)) {
            return new ImperialUnits();
        }
        return new SIUnitsWithCentimeters();
    }

    @Override
    public void setUnits(Units units) throws RepositoryException {
        String unitType = (units instanceof SIUnitsWithCentimeters) ? SI_UNITS : IMPERIAL_UNITS;
        configRepository.setProperty(UNIT_SYSTEM_KEY, unitType);
    }

    @Override
    public String getLanguage() throws RepositoryException {
        return configRepository.getProperty(LANGUAGE_KEY);
    }

    @Override
    public void setLanguage(String language) throws RepositoryException {
        configRepository.setProperty(LANGUAGE_KEY, language);
    }
}