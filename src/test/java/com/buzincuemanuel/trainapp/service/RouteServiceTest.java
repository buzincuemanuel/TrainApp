package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.Route;
import com.buzincuemanuel.trainapp.repository.RouteRepository;
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
@DisplayName("RouteService tests")
class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    @Nested
    @DisplayName("When saving a route")
    class WhenSaving {

        @Test
        @DisplayName("Should return saved route with correct fields")
        void shouldReturnSavedRoute() {
            Route route = Route.builder()
                    .name("Cluj-Bucuresti")
                    .build();

            when(routeRepository.save(route)).thenReturn(route);

            Route result = routeService.save(route);

            assertThat(result.getName()).isEqualTo("Cluj-Bucuresti");
            verify(routeRepository).save(route);
        }
    }

    @Nested
    @DisplayName("When finding all routes")
    class WhenFindingAll {

        @Test
        @DisplayName("Should return all routes")
        void shouldReturnAllRoutes() {
            List<Route> routes = List.of(
                    Route.builder().name("Cluj-Bucuresti").build(),
                    Route.builder().name("Cluj-Brasov").build()
            );

            when(routeRepository.findAll()).thenReturn(routes);

            List<Route> result = routeService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("Cluj-Bucuresti");
            verify(routeRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no routes exist")
        void shouldReturnEmptyList_whenNoRoutesExist() {
            when(routeRepository.findAll()).thenReturn(List.of());

            List<Route> result = routeService.findAll();

            assertThat(result).isEmpty();
            verify(routeRepository).findAll();
        }
    }

    @Nested
    @DisplayName("When finding route by id")
    class WhenFindingById {

        @Test
        @DisplayName("Should return route when it exists")
        void shouldReturnRoute_whenExists() {
            Route route = Route.builder()
                    .name("Cluj-Bucuresti")
                    .build();

            when(routeRepository.findById(1L)).thenReturn(Optional.of(route));

            Optional<Route> result = routeService.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Cluj-Bucuresti");
            verify(routeRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when route does not exist")
        void shouldReturnEmpty_whenNotExists() {
            when(routeRepository.findById(1L)).thenReturn(Optional.empty());

            Optional<Route> result = routeService.findById(1L);

            assertThat(result).isEmpty();
            verify(routeRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("When deleting a route")
    class WhenDeleting {

        @Test
        @DisplayName("Should call repository deleteById")
        void shouldCallRepositoryDelete() {
            routeService.deleteById(1L);

            verify(routeRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("When updating a route")
    class WhenUpdating {

        @Test
        @DisplayName("Should set id and return updated route")
        void shouldSetIdAndReturnUpdatedRoute() {
            Route route = Route.builder()
                    .name("Cluj-Bucuresti")
                    .build();

            when(routeRepository.save(route)).thenReturn(route);

            Route result = routeService.update(1L, route);

            assertThat(result.getName()).isEqualTo("Cluj-Bucuresti");
            verify(routeRepository).save(route);
        }
    }
}