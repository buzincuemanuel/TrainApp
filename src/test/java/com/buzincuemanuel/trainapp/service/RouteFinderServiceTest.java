package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.*;
import com.buzincuemanuel.trainapp.repository.RouteRepository;
import com.buzincuemanuel.trainapp.repository.TrainScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RouteFinderService tests")
class RouteFinderServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private TrainScheduleRepository trainScheduleRepository;

    @InjectMocks
    private RouteFinderService routeFinderService;

    private Station buildStation(String name) {
        return Station.builder().name(name).build();
    }

    private RouteStop buildStop(Route route, Station station, int order, int minutes) {
        return RouteStop.builder()
                .route(route)
                .station(station)
                .stopOrder(order)
                .minutesFromStart(minutes)
                .build();
    }

    private Route buildRoute(String name, List<RouteStop> stops) {
        Route route = Route.builder().name(name).stops(new ArrayList<>(stops)).build();
        stops.forEach(s -> s.setRoute(route));
        return route;
    }

    private TrainSchedule buildSchedule(Route route, LocalTime departureTime) {
        return TrainSchedule.builder()
                .route(route)
                .train(Train.builder().name("IR 1234").capacity(100).build())
                .departureTime(departureTime)
                .delayMinutes(0)
                .build();
    }

    @Nested
    @DisplayName("When finding direct routes")
    class WhenFindingDirectRoutes {

        @Test
        @DisplayName("Should return one result when direct route exists")
        void shouldReturnOneResult_whenDirectRouteExists() {
            Station cluj = buildStation("Cluj");
            Station bucuresti = buildStation("Bucuresti");

            Route route = new Route();
            route.setName("Cluj-Bucuresti");
            RouteStop stopA = buildStop(route, cluj, 0, 0);
            RouteStop stopB = buildStop(route, bucuresti, 1, 240);
            route.setStops(new ArrayList<>(List.of(stopA, stopB)));

            TrainSchedule schedule = buildSchedule(route, LocalTime.of(8, 0));

            when(routeRepository.findValidRoutesBetweenStations("Cluj", "Bucuresti"))
                    .thenReturn(List.of(route));
            when(trainScheduleRepository.findByRouteIn(List.of(route)))
                    .thenReturn(List.of(schedule));

            List<List<TrainSchedule>> results = routeFinderService.findAllRoutes("Cluj", "Bucuresti");

            assertThat(results).hasSize(1);
            assertThat(results.get(0)).hasSize(1);
            assertThat(results.get(0).get(0)).isEqualTo(schedule);
        }

        @Test
        @DisplayName("Should return empty list when no direct route exists")
        void shouldReturnEmpty_whenNoDirectRouteExists() {
            when(routeRepository.findValidRoutesBetweenStations("Cluj", "Bucuresti"))
                    .thenReturn(List.of());
            when(trainScheduleRepository.findByRouteIn(List.of()))
                    .thenReturn(List.of());
            when(routeRepository.findRoutesPassingThroughStation("Cluj"))
                    .thenReturn(List.of());

            List<List<TrainSchedule>> results = routeFinderService.findAllRoutes("Cluj", "Bucuresti");

            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("When finding routes with one transfer")
    class WhenFindingRoutesWithOneTransfer {

        @Test
        @DisplayName("Should return route with one transfer when connection exists")
        void shouldReturnRouteWithOneTransfer_whenConnectionExists() {
            Station cluj = buildStation("Cluj");
            Station brasov = buildStation("Brasov");
            Station bucuresti = buildStation("Bucuresti");

            Route route1 = new Route();
            route1.setName("Cluj-Brasov");
            RouteStop stop1A = buildStop(route1, cluj, 0, 0);
            RouteStop stop1B = buildStop(route1, brasov, 1, 120);
            route1.setStops(new ArrayList<>(List.of(stop1A, stop1B)));

            Route route2 = new Route();
            route2.setName("Brasov-Bucuresti");
            RouteStop stop2A = buildStop(route2, brasov, 0, 0);
            RouteStop stop2B = buildStop(route2, bucuresti, 1, 180);
            route2.setStops(new ArrayList<>(List.of(stop2A, stop2B)));

            TrainSchedule firstSchedule = buildSchedule(route1, LocalTime.of(8, 0));
            TrainSchedule secondSchedule = buildSchedule(route2, LocalTime.of(10, 30));

            when(routeRepository.findValidRoutesBetweenStations("Cluj", "Bucuresti"))
                    .thenReturn(List.of());
            when(trainScheduleRepository.findByRouteIn(List.of()))
                    .thenReturn(List.of());
            when(routeRepository.findRoutesPassingThroughStation("Cluj"))
                    .thenReturn(List.of(route1));
            when(trainScheduleRepository.findByRouteIn(List.of(route1)))
                    .thenReturn(List.of(firstSchedule));
            when(routeRepository.findValidRoutesBetweenStations("Brasov", "Bucuresti"))
                    .thenReturn(List.of(route2));
            when(trainScheduleRepository.findByRouteIn(List.of(route2)))
                    .thenReturn(List.of(secondSchedule));

            List<List<TrainSchedule>> results = routeFinderService.findAllRoutes("Cluj", "Bucuresti");

            assertThat(results).hasSize(1);
            assertThat(results.get(0)).hasSize(2);
            assertThat(results.get(0).get(0)).isEqualTo(firstSchedule);
            assertThat(results.get(0).get(1)).isEqualTo(secondSchedule);
        }

        @Test
        @DisplayName("Should not return route when transfer time is insufficient")
        void shouldNotReturnRoute_whenTransferTimeInsufficient() {
            Station cluj = buildStation("Cluj");
            Station brasov = buildStation("Brasov");
            Station bucuresti = buildStation("Bucuresti");

            Route route1 = new Route();
            route1.setName("Cluj-Brasov");
            RouteStop stop1A = buildStop(route1, cluj, 0, 0);
            RouteStop stop1B = buildStop(route1, brasov, 1, 120);
            route1.setStops(new ArrayList<>(List.of(stop1A, stop1B)));

            Route route2 = new Route();
            route2.setName("Brasov-Bucuresti");
            RouteStop stop2A = buildStop(route2, brasov, 0, 0);
            RouteStop stop2B = buildStop(route2, bucuresti, 1, 180);
            route2.setStops(new ArrayList<>(List.of(stop2A, stop2B)));

            TrainSchedule firstSchedule = buildSchedule(route1, LocalTime.of(8, 0));
            // al doilea tren pleaca la 10:05 — sosire in Brasov 10:00, minim 15 min => invalid
            TrainSchedule secondSchedule = buildSchedule(route2, LocalTime.of(10, 5));

            when(routeRepository.findValidRoutesBetweenStations("Cluj", "Bucuresti"))
                    .thenReturn(List.of());
            when(trainScheduleRepository.findByRouteIn(List.of()))
                    .thenReturn(List.of());
            when(routeRepository.findRoutesPassingThroughStation("Cluj"))
                    .thenReturn(List.of(route1));
            when(trainScheduleRepository.findByRouteIn(List.of(route1)))
                    .thenReturn(List.of(firstSchedule));
            when(routeRepository.findValidRoutesBetweenStations("Brasov", "Bucuresti"))
                    .thenReturn(List.of(route2));
            when(trainScheduleRepository.findByRouteIn(List.of(route2)))
                    .thenReturn(List.of(secondSchedule));

            List<List<TrainSchedule>> results = routeFinderService.findAllRoutes("Cluj", "Bucuresti");

            assertThat(results).isEmpty();
        }
    }

    @Nested
    @DisplayName("When finding routes with two transfers")
    class WhenFindingRoutesWithTwoTransfers {

        @Test
        @DisplayName("Should return route with two transfers when connection exists")
        void shouldReturnRouteWithTwoTransfers_whenConnectionExists() {
            Station cluj = buildStation("Cluj");
            Station brasov = buildStation("Brasov");
            Station sinaia = buildStation("Sinaia");
            Station bucuresti = buildStation("Bucuresti");

            Route route1 = new Route();
            route1.setName("Cluj-Brasov");
            RouteStop stop1A = buildStop(route1, cluj, 0, 0);
            RouteStop stop1B = buildStop(route1, brasov, 1, 120);
            route1.setStops(new ArrayList<>(List.of(stop1A, stop1B)));

            Route route2 = new Route();
            route2.setName("Brasov-Sinaia");
            RouteStop stop2A = buildStop(route2, brasov, 0, 0);
            RouteStop stop2B = buildStop(route2, sinaia, 1, 60);
            route2.setStops(new ArrayList<>(List.of(stop2A, stop2B)));

            Route route3 = new Route();
            route3.setName("Sinaia-Bucuresti");
            RouteStop stop3A = buildStop(route3, sinaia, 0, 0);
            RouteStop stop3B = buildStop(route3, bucuresti, 1, 90);
            route3.setStops(new ArrayList<>(List.of(stop3A, stop3B)));

            TrainSchedule firstSchedule = buildSchedule(route1, LocalTime.of(8, 0));
            TrainSchedule secondSchedule = buildSchedule(route2, LocalTime.of(10, 30));
            TrainSchedule thirdSchedule = buildSchedule(route3, LocalTime.of(12, 0));

            when(routeRepository.findValidRoutesBetweenStations("Cluj", "Bucuresti"))
                    .thenReturn(List.of());
            when(trainScheduleRepository.findByRouteIn(List.of()))
                    .thenReturn(List.of());
            when(routeRepository.findRoutesPassingThroughStation("Cluj"))
                    .thenReturn(List.of(route1));
            when(trainScheduleRepository.findByRouteIn(List.of(route1)))
                    .thenReturn(List.of(firstSchedule));
            when(routeRepository.findValidRoutesBetweenStations("Brasov", "Bucuresti"))
                    .thenReturn(List.of());
            when(routeRepository.findRoutesPassingThroughStation("Brasov"))
                    .thenReturn(List.of(route2));
            when(trainScheduleRepository.findByRouteIn(List.of(route2)))
                    .thenReturn(List.of(secondSchedule));
            when(routeRepository.findValidRoutesBetweenStations("Sinaia", "Bucuresti"))
                    .thenReturn(List.of(route3));
            when(trainScheduleRepository.findByRouteIn(List.of(route3)))
                    .thenReturn(List.of(thirdSchedule));

            List<List<TrainSchedule>> results = routeFinderService.findAllRoutes("Cluj", "Bucuresti");

            assertThat(results).hasSize(1);
            assertThat(results.get(0)).hasSize(3);
            assertThat(results.get(0).get(0)).isEqualTo(firstSchedule);
            assertThat(results.get(0).get(1)).isEqualTo(secondSchedule);
            assertThat(results.get(0).get(2)).isEqualTo(thirdSchedule);
        }
    }

    @Nested
    @DisplayName("When stations are the same")
    class WhenStationsAreTheSame {

        @Test
        @DisplayName("Should return empty list when from and to are the same")
        void shouldReturnEmpty_whenFromAndToAreTheSame() {
            when(routeRepository.findValidRoutesBetweenStations("Cluj", "Cluj"))
                    .thenReturn(List.of());
            when(trainScheduleRepository.findByRouteIn(List.of()))
                    .thenReturn(List.of());
            when(routeRepository.findRoutesPassingThroughStation("Cluj"))
                    .thenReturn(List.of());

            List<List<TrainSchedule>> results = routeFinderService.findAllRoutes("Cluj", "Cluj");

            assertThat(results).isEmpty();
        }
    }
}