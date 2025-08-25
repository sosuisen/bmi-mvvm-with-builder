package com.example.service;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.Languages;
import com.example.domain.model.unit.ImperialUnits;
import com.example.domain.model.unit.SIUnitsWithCentimeters;
import com.example.domain.model.unit.UnitSystem;
import com.example.domain.repository.ConfigRepository;
import com.example.presentation.helpers.I18n;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigServiceImplTest {

    @Mock
    private ConfigRepository configRepository;

    @InjectMocks
    private ConfigServiceImpl configService;

    @Test
    void getUnitSystem_whenRepoIsNull_returnsDefault() throws RepositoryException {
        // Given
        when(configRepository.getConfig("unit.system")).thenReturn(null);

        // When
        UnitSystem result = configService.getUnitSystem();

        // Then
        assertInstanceOf(SIUnitsWithCentimeters.class, result);
    }

    @Test
    void getUnitSystem_whenRepoHasValidValue_returnsCorrectUnitSystem() throws RepositoryException {
        // Given
        when(configRepository.getConfig("unit.system")).thenReturn("ImperialUnits");

        // When
        UnitSystem result = configService.getUnitSystem();

        // Then
        assertInstanceOf(ImperialUnits.class, result);
    }

    @Test
    void getUnitSystem_whenRepoHasInvalidValue_throwsException() throws RepositoryException {
        // Given
        when(configRepository.getConfig("unit.system")).thenReturn("InvalidUnitSystem");

        // When & Then
        assertThrows(RepositoryException.class, () -> configService.getUnitSystem());
    }

    @Test
    void setUnitSystem_callsRepository() throws RepositoryException {
        // Given
        UnitSystem unitSystem = new ImperialUnits();

        // When
        configService.setUnitSystem(unitSystem);

        // Then
        verify(configRepository, times(1)).setConfig("unit.system", "ImperialUnits");
    }

    @Test
    void getLanguage_whenRepoIsNull_fallsBackToI18nLocale() throws RepositoryException {
        // Given
        when(configRepository.getConfig("language")).thenReturn(null);
        I18n mockI18n = mock(I18n.class);
        // Mock OS locale
        when(mockI18n.getCurrentLocale()).thenReturn(Locale.of("ja"));

        try (MockedStatic<I18n> mockedStatic = Mockito.mockStatic(I18n.class)) {
            mockedStatic.when(I18n::getInstance).thenReturn(mockI18n);

            // When
            Languages result = configService.getLanguage();

            // Then
            assertEquals(Languages.JA, result);
        }
    }

    @Test
    void getLanguage_whenRepoHasValidValue_returnsCorrectLanguage() throws RepositoryException {
        // Given
        when(configRepository.getConfig("language")).thenReturn("en");

        // When
        Languages result = configService.getLanguage();

        // Then
        assertEquals(Languages.EN, result);
    }

    @Test
    void getLanguage_whenRepoHasInvalidValue_returnsDefault() throws RepositoryException {
        // Given
        when(configRepository.getConfig("language")).thenReturn("xx");

        // When
        Languages result = configService.getLanguage();

        // Then
        assertEquals(Languages.getDefaultLanguages(), result);
    }

    @Test
    void setLanguage_callsRepository() throws RepositoryException {
        // Given
        Languages language = Languages.JA;

        // When
        configService.setLanguage(language);

        // Then
        verify(configRepository, times(1)).setConfig("language", "ja");
    }
}
