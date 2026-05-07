package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.Train;
import com.buzincuemanuel.trainapp.repository.TrainRepository;
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
@DisplayName("TrainService tests")
class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainService trainService;

    @Nested
    @DisplayName("When saving a train")
    class WhenSaving {

        @Test
        @DisplayName("Should return saved train with correct fields")
        void shouldReturnSavedTrain() {
            Train train = Train.builder()
                    .name("IR 1234")
                    .capacity(100)
                    .build();

            when(trainRepository.save(train)).thenReturn(train);

            Train result = trainService.save(train);

            assertThat(result.getName()).isEqualTo("IR 1234");
            assertThat(result.getCapacity()).isEqualTo(100);
            verify(trainRepository).save(train);
        }
    }

    @Nested
    @DisplayName("When finding all trains")
    class WhenFindingAll {

        @Test
        @DisplayName("Should return all trains")
        void shouldReturnAllTrains() {
            List<Train> trains = List.of(
                    Train.builder().name("IR 1234").capacity(100).build(),
                    Train.builder().name("IR 5678").capacity(200).build()
            );

            when(trainRepository.findAll()).thenReturn(trains);

            List<Train> result = trainService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("IR 1234");
            verify(trainRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no trains exist")
        void shouldReturnEmptyList_whenNoTrainsExist() {
            when(trainRepository.findAll()).thenReturn(List.of());

            List<Train> result = trainService.findAll();

            assertThat(result).isEmpty();
            verify(trainRepository).findAll();
        }
    }

    @Nested
    @DisplayName("When finding train by id")
    class WhenFindingById {

        @Test
        @DisplayName("Should return train when it exists")
        void shouldReturnTrain_whenExists() {
            Train train = Train.builder()
                    .name("IR 1234")
                    .capacity(100)
                    .build();

            when(trainRepository.findById(1L)).thenReturn(Optional.of(train));

            Optional<Train> result = trainService.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("IR 1234");
            verify(trainRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when train does not exist")
        void shouldReturnEmpty_whenNotExists() {
            when(trainRepository.findById(1L)).thenReturn(Optional.empty());

            Optional<Train> result = trainService.findById(1L);

            assertThat(result).isEmpty();
            verify(trainRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("When deleting a train")
    class WhenDeleting {

        @Test
        @DisplayName("Should call repository deleteById")
        void shouldCallRepositoryDelete() {
            trainService.deleteById(1L);

            verify(trainRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("When updating a train")
    class WhenUpdating {

        @Test
        @DisplayName("Should set id and return updated train")
        void shouldSetIdAndReturnUpdatedTrain() {
            Train train = Train.builder()
                    .name("IR 1234")
                    .capacity(100)
                    .build();

            when(trainRepository.save(train)).thenReturn(train);

            Train result = trainService.update(1L, train);

            assertThat(result.getName()).isEqualTo("IR 1234");
            verify(trainRepository).save(train);
        }
    }
}