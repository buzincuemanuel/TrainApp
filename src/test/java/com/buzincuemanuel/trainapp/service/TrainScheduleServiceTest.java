package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.TrainSchedule;
import com.buzincuemanuel.trainapp.repository.TrainScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainScheduleService tests")
class TrainScheduleServiceTest {

    @Mock
    private TrainScheduleRepository trainScheduleRepository;

    @InjectMocks
    private TrainScheduleService trainScheduleService;

    @Nested
    @DisplayName("When saving a train schedule")
    class WhenSaving {

        @Test
        @DisplayName("Should return saved train schedule with correct fields")
        void shouldReturnSavedTrainSchedule() {
            TrainSchedule schedule = TrainSchedule.builder()
                    .departureTime(LocalTime.of(8, 0))
                    .build();

            when(trainScheduleRepository.save(schedule)).thenReturn(schedule);

            TrainSchedule result = trainScheduleService.save(schedule);

            assertThat(result.getDepartureTime()).isEqualTo(LocalTime.of(8, 0));
            verify(trainScheduleRepository).save(schedule);
        }
    }

    @Nested
    @DisplayName("When finding all train schedules")
    class WhenFindingAll {

        @Test
        @DisplayName("Should return all train schedules")
        void shouldReturnAllTrainSchedules() {
            List<TrainSchedule> schedules = List.of(
                    TrainSchedule.builder().departureTime(LocalTime.of(8, 0)).build(),
                    TrainSchedule.builder().departureTime(LocalTime.of(14, 0)).build()
            );

            when(trainScheduleRepository.findAll()).thenReturn(schedules);

            List<TrainSchedule> result = trainScheduleService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getDepartureTime()).isEqualTo(LocalTime.of(8, 0));
            verify(trainScheduleRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no schedules exist")
        void shouldReturnEmptyList_whenNoSchedulesExist() {
            when(trainScheduleRepository.findAll()).thenReturn(List.of());

            List<TrainSchedule> result = trainScheduleService.findAll();

            assertThat(result).isEmpty();
            verify(trainScheduleRepository).findAll();
        }
    }

    @Nested
    @DisplayName("When finding train schedule by id")
    class WhenFindingById {

        @Test
        @DisplayName("Should return schedule when it exists")
        void shouldReturnSchedule_whenExists() {
            TrainSchedule schedule = TrainSchedule.builder()
                    .departureTime(LocalTime.of(8, 0))
                    .build();

            when(trainScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

            Optional<TrainSchedule> result = trainScheduleService.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getDepartureTime()).isEqualTo(LocalTime.of(8, 0));
            verify(trainScheduleRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when schedule does not exist")
        void shouldReturnEmpty_whenNotExists() {
            when(trainScheduleRepository.findById(1L)).thenReturn(Optional.empty());

            Optional<TrainSchedule> result = trainScheduleService.findById(1L);

            assertThat(result).isEmpty();
            verify(trainScheduleRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("When deleting a train schedule")
    class WhenDeleting {

        @Test
        @DisplayName("Should call repository deleteById")
        void shouldCallRepositoryDelete() {
            trainScheduleService.deleteById(1L);

            verify(trainScheduleRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("When updating a train schedule")
    class WhenUpdating {

        @Test
        @DisplayName("Should set id and return updated schedule")
        void shouldSetIdAndReturnUpdatedSchedule() {
            TrainSchedule schedule = TrainSchedule.builder()
                    .departureTime(LocalTime.of(8, 0))
                    .build();

            when(trainScheduleRepository.save(schedule)).thenReturn(schedule);

            TrainSchedule result = trainScheduleService.update(1L, schedule);

            assertThat(result.getDepartureTime()).isEqualTo(LocalTime.of(8, 0));
            verify(trainScheduleRepository).save(schedule);
        }
    }
}