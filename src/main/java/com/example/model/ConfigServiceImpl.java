package com.example.model;

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
    private static final String DEFAULT_LANGUAGE = "en";
    
    private final ConfigRepository configRepository;
    private Units currentUnits;
    private String currentLanguage;

    public ConfigServiceImpl(ConfigRepository configRepository) {
        this.configRepository = configRepository;
        this.currentUnits = new SIUnitsWithCentimeters();
        this.currentLanguage = DEFAULT_LANGUAGE;
    }

    @Override
    public Units getUnits() throws RepositoryException {
        return currentUnits;
    }

    @Override
    public void setUnits(Units units) throws RepositoryException {
        this.currentUnits = units;
        String unitType = (units instanceof SIUnitsWithCentimeters) ? SI_UNITS : IMPERIAL_UNITS;
        configRepository.setProperty(UNIT_SYSTEM_KEY, unitType);
        saveConfig();
    }

    @Override
    public String getLanguage() throws RepositoryException {
        return currentLanguage;
    }

    @Override
    public void setLanguage(String language) throws RepositoryException {
        this.currentLanguage = language;
        configRepository.setProperty(LANGUAGE_KEY, language);
        saveConfig();
    }

    @Override
    public void saveConfig() throws RepositoryException {
        configRepository.save();
    }

    @Override
    public void loadConfig() throws RepositoryException {
        configRepository.load();
        String unitType = configRepository.getProperty(UNIT_SYSTEM_KEY);
        
        if (IMPERIAL_UNITS.equals(unitType)) {
            this.currentUnits = new ImperialUnits();
        } else {
            this.currentUnits = new SIUnitsWithCentimeters();
        }
        
        String language = configRepository.getProperty(LANGUAGE_KEY);
        this.currentLanguage = (language != null) ? language : DEFAULT_LANGUAGE;
    }
}