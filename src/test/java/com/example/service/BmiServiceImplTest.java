package com.example.service;

import com.example.domain.exception.RepositoryException;
import com.example.domain.model.BmiRecord;
import com.example.domain.model.BmiRecordOrder;
import com.example.domain.repository.BmiRepository;
import com.example.domain.service.BmiRecordWithDiff;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BmiServiceImplTest {

    @Mock
    private BmiRepository bmiRepository;

    @InjectMocks
    private BmiServiceImpl bmiService;

    @Test
    void calculateBmi_withValidInputs_returnsBmi() {
        // Given
        double height = 1.75;
        double weight = 70;

        // When
        Optional<Double> result = bmiService.calculateBmi(height, weight);

        // Then
        assertTrue(result.isPresent());
        assertEquals(22.857, result.get(), 0.001);
    }

    @Test
    void calculateBmi_withInvalidInputs_returnsEmpty() {
        // Given
        double height = 0;
        double weight = 70;

        // When
        Optional<Double> result = bmiService.calculateBmi(height, weight);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void removeRecord_callsRepository() throws RepositoryException {
        // Given
        int id = 1;

        // When
        bmiService.removeRecord(id);

        // Then
        verify(bmiRepository, times(1)).removeRecord(id);
    }

    @Test
    void removeRecord_whenRepositoryThrowsException_propagatesException() throws RepositoryException {
        // Given
        int id = 1;
        doThrow(new RepositoryException("DB error")).when(bmiRepository).removeRecord(id);

        // When & Then
        assertThrows(RepositoryException.class, () -> bmiService.removeRecord(id));
    }

    @Test
    void removeAllRecords_callsRepository() throws RepositoryException {
        // When
        bmiService.removeAllRecords();

        // Then
        verify(bmiRepository, times(1)).removeAllRecords();
    }

    @Test
    void removeAllRecords_whenRepositoryThrowsException_propagatesException() throws RepositoryException {
        // Given
        doThrow(new RepositoryException("DB error")).when(bmiRepository).removeAllRecords();

        // When & Then
        assertThrows(RepositoryException.class, () -> bmiService.removeAllRecords());
    }

    @Test
    void loadRecords_withEmptyList_returnsEmptyList() throws RepositoryException {
        // Given
        when(bmiRepository.loadBmiRecords(eq(BmiRecordOrder.DATE_DESC), anyInt())).thenReturn(new ArrayList<>());

        // When
        List<BmiRecordWithDiff> result = bmiService.loadRecords(50);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void loadRecords_withSingleRecord_returnsListWithOneElemen_withCorrectDateAndDiff() throws RepositoryException {
        // Given
        BmiRecord record = new BmiRecord(1, 1.75, 70, LocalDate.now());
        when(bmiRepository.loadBmiRecords(eq(BmiRecordOrder.DATE_DESC), anyInt())).thenReturn(List.of(record));

        // When
        List<BmiRecordWithDiff> result = bmiService.loadRecords(50);

        // Then
        assertEquals(1, result.size());
        assertEquals(record.date(), result.get(0).date());
        assertEquals(0, result.get(0).diff());
        assertNull(result.get(0).prevRecord());
    }

    @Test
    void loadRecords_withMultipleRecords_returnsCorrectlyOrderedListWithDiffs() throws RepositoryException {
        // Given
        BmiRecord record1 = new BmiRecord(1, 1.70, 65, LocalDate.of(2023, 1, 10));
        BmiRecord record2 = new BmiRecord(2, 1.70, 68, LocalDate.of(2023, 1, 15));
        BmiRecord record3 = new BmiRecord(3, 1.70, 67, LocalDate.of(2023, 1, 20));
        List<BmiRecord> recordsFromRepo = List.of(record3, record2, record1);
        when(bmiRepository.loadBmiRecords(eq(BmiRecordOrder.DATE_DESC), anyInt())).thenReturn(recordsFromRepo);

        // When
        List<BmiRecordWithDiff> result = bmiService.loadRecords(50);

        // Then
        assertEquals(3, result.size());

        // Check order
        assertEquals(record3.id(), result.get(0).id());
        assertEquals(record2.id(), result.get(1).id());
        assertEquals(record1.id(), result.get(2).id());

        // Check diffs
        assertEquals(record2, result.get(0).prevRecord());
        assertEquals(record1, result.get(1).prevRecord());
        assertNull(result.get(2).prevRecord());
    }

    @Test
    void loadRecords_passesCorrectLimitToRepository() throws RepositoryException {
        // Given
        int expectedLimit = 25;
        ArgumentCaptor<Integer> limitCaptor = ArgumentCaptor.forClass(Integer.class);
        when(bmiRepository.loadBmiRecords(eq(BmiRecordOrder.DATE_DESC), limitCaptor.capture()))
                .thenReturn(new ArrayList<>());

        // When
        bmiService.loadRecords(expectedLimit);

        // Then
        verify(bmiRepository, times(1)).loadBmiRecords(eq(BmiRecordOrder.DATE_DESC), anyInt());
        assertEquals(expectedLimit, limitCaptor.getValue());
    }

    @Test
    void upsertRecord_callsRepository() throws RepositoryException {
        // Given
        double height = 1.80;
        double weight = 80;
        LocalDate date = LocalDate.now();

        // When
        bmiService.upsertRecord(height, weight, date);

        // Then
        verify(bmiRepository, times(1)).upsertBmiRecord(height, weight, date);
    }
}
