package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.RouteStop;
import com.buzincuemanuel.trainapp.repository.RouteStopRepository;
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
@DisplayName("RouteStopService tests")
class RouteStopServiceTest {

    @Mock
    private RouteStopRepository routeStopRepository;

    @InjectMocks
    private RouteStopService routeStopService;

    @Nested
    @DisplayName("When saving a route stop")
    class WhenSaving {

        @Test
        @DisplayName("Should return saved route stop with correct fields")
        void shouldReturnSavedRouteStop() {
            RouteStop routeStop = RouteStop.builder()
                    .stopOrder(0)
                    .minutesFromStart(0)
                    .build();

            when(routeStopRepository.save(routeStop)).thenReturn(routeStop);

            RouteStop result = routeStopService.save(routeStop);

            assertThat(result.getStopOrder()).isEqualTo(0);
            assertThat(result.getMinutesFromStart()).isEqualTo(0);
            verify(routeStopRepository).save(routeStop);
        }
    }

    @Nested
    @DisplayName("When finding all route stops")
    class WhenFindingAll {

        @Test
        @DisplayName("Should return all route stops")
        void shouldReturnAllRouteStops() {
            List<RouteStop> stops = List.of(
                    RouteStop.builder().stopOrder(0).minutesFromStart(0).build(),
                    RouteStop.builder().stopOrder(1).minutesFromStart(120).build()
            );

            when(routeStopRepository.findAll()).thenReturn(stops);

            List<RouteStop> result = routeStopService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(1).getMinutesFromStart()).isEqualTo(120);
            verify(routeStopRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no route stops exist")
        void shouldReturnEmptyList_whenNoRouteStopsExist() {
            when(routeStopRepository.findAll()).thenReturn(List.of());

            List<RouteStop> result = routeStopService.findAll();

            assertThat(result).isEmpty();
            verify(routeStopRepository).findAll();
        }
    }

    @Nested
    @DisplayName("When finding route stop by id")
    class WhenFindingById {

        @Test
        @DisplayName("Should return route stop when it exists")
        void shouldReturnRouteStop_whenExists() {
            RouteStop routeStop = RouteStop.builder()
                    .stopOrder(0)
                    .minutesFromStart(0)
                    .build();

            when(routeStopRepository.findById(1L)).thenReturn(Optional.of(routeStop));

            Optional<RouteStop> result = routeStopService.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getStopOrder()).isEqualTo(0);
            verify(routeStopRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when route stop does not exist")
        void shouldReturnEmpty_whenNotExists() {
            when(routeStopRepository.findById(1L)).thenReturn(Optional.empty());

            Optional<RouteStop> result = routeStopService.findById(1L);

            assertThat(result).isEmpty();
            verify(routeStopRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("When deleting a route stop")
    class WhenDeleting {

        @Test
        @DisplayName("Should call repository deleteById")
        void shouldCallRepositoryDelete() {
            routeStopService.deleteById(1L);

            verify(routeStopRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("When updating a route stop")
    class WhenUpdating {

        @Test
        @DisplayName("Should set id and return updated route stop")
        void shouldSetIdAndReturnUpdatedRouteStop() {
            RouteStop routeStop = RouteStop.builder()
                    .stopOrder(0)
                    .minutesFromStart(0)
                    .build();

            when(routeStopRepository.save(routeStop)).thenReturn(routeStop);

            RouteStop result = routeStopService.update(1L, routeStop);

            assertThat(result.getStopOrder()).isEqualTo(0);
            verify(routeStopRepository).save(routeStop);
        }
    }
}