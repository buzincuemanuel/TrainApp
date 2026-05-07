package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.Station;
import com.buzincuemanuel.trainapp.repository.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StationService tests")
class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    @Nested
    @DisplayName("When saving a station")
    class WhenSaving {

        @Test
        @DisplayName("Should return saved station with correct fields")
        void shouldReturnSavedStation() {
            Station station = Station.builder()
                    .name("Cluj-Napoca")
                    .build();

            when(stationRepository.save(station)).thenReturn(station);

            Station result = stationService.save(station);

            assertThat(result.getName()).isEqualTo("Cluj-Napoca");
            verify(stationRepository).save(station);
        }
    }

    @Nested
    @DisplayName("When finding all stations")
    class WhenFindingAll {

        @Test
        @DisplayName("Should return all stations")
        void shouldReturnAllStations() {
            List<Station> stations = List.of(
                    Station.builder().name("Cluj-Napoca").build(),
                    Station.builder().name("Brasov").build()
            );

            when(stationRepository.findAll()).thenReturn(stations);

            List<Station> result = stationService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("Cluj-Napoca");
            verify(stationRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no stations exist")
        void shouldReturnEmptyList_whenNoStationsExist() {
            when(stationRepository.findAll()).thenReturn(List.of());

            List<Station> result = stationService.findAll();

            assertThat(result).isEmpty();
            verify(stationRepository).findAll();
        }
    }

    @Nested
    @DisplayName("When finding station by id")
    class WhenFindingById {

        @Test
        @DisplayName("Should return station when it exists")
        void shouldReturnStation_whenExists() {
            Station station = Station.builder()
                    .name("Cluj-Napoca")
                    .build();

            when(stationRepository.findById(1L)).thenReturn(Optional.of(station));

            Optional<Station> result = stationService.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Cluj-Napoca");
            verify(stationRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when station does not exist")
        void shouldReturnEmpty_whenNotExists() {
            when(stationRepository.findById(1L)).thenReturn(Optional.empty());

            Optional<Station> result = stationService.findById(1L);

            assertThat(result).isEmpty();
            verify(stationRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("When deleting a station")
    class WhenDeleting {

        @Test
        @DisplayName("Should call repository deleteById")
        void shouldCallRepositoryDelete() {
            stationService.deleteById(1L);

            verify(stationRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("When updating a station")
    class WhenUpdating {

        @Test
        @DisplayName("Should set id and return updated station")
        void shouldSetIdAndReturnUpdatedStation() {
            Station station = Station.builder()
                    .name("Cluj-Napoca")
                    .build();

            when(stationRepository.save(station)).thenReturn(station);

            Station result = stationService.update(1L, station);

            assertThat(result.getName()).isEqualTo("Cluj-Napoca");
            verify(stationRepository).save(station);
        }
    }
}