package com.example.service;

import com.example.domain.model.unit.ImperialUnits;
import com.example.domain.model.unit.SIUnitsWithCentimeters;
import com.example.domain.model.unit.Units;
import com.example.domain.repository.ConfigRepository;
import com.example.domain.exception.RepositoryException;
import com.example.domain.service.ConfigService;

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
        String unitType = configRepository.getConfig(UNIT_SYSTEM_KEY);
        if (IMPERIAL_UNITS.equals(unitType)) {
            return new ImperialUnits();
        }
        return new SIUnitsWithCentimeters();
    }

    @Override
    public void setUnits(Units units) throws RepositoryException {
        String unitType = (units instanceof SIUnitsWithCentimeters) ? SI_UNITS : IMPERIAL_UNITS;
        configRepository.setConfig(UNIT_SYSTEM_KEY, unitType);
    }

    @Override
    public String getLanguage() throws RepositoryException {
        return configRepository.getConfig(LANGUAGE_KEY);
    }

    @Override
    public void setLanguage(String language) throws RepositoryException {
        configRepository.setConfig(LANGUAGE_KEY, language);
    }
}