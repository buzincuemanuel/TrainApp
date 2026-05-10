package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.Route;
import com.buzincuemanuel.trainapp.model.RouteStop;
import com.buzincuemanuel.trainapp.model.TrainSchedule;
import com.buzincuemanuel.trainapp.repository.RouteRepository;
import com.buzincuemanuel.trainapp.repository.TrainScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteFinderService {

    private final RouteRepository routeRepository;
    private final TrainScheduleRepository trainScheduleRepository;

    private static final int MIN_TRANSFER_MINUTES = 15;

    public List<List<TrainSchedule>> findAllRoutes(String from, String to) {
        List<List<TrainSchedule>> results = new ArrayList<>();
        results.addAll(findDirectRoutes(from, to));
        results.addAll(findRoutesWithOneTransfer(from, to));
        results.addAll(findRoutesWithTwoTransfers(from, to));
        return results;
    }

    private List<List<TrainSchedule>> findDirectRoutes(String from, String to) {
        List<List<TrainSchedule>> results = new ArrayList<>();

        for (TrainSchedule schedule : getDirectSchedules(from, to)) {
            results.add(List.of(schedule));
        }

        return results;
    }

    private List<List<TrainSchedule>> findRoutesWithOneTransfer(String from, String to) {
        List<List<TrainSchedule>> results = new ArrayList<>();

        for (TrainSchedule firstSchedule : getSchedulesPassingThrough(from)) {
            List<RouteStop> stopsAfterA = firstSchedule.getRoute().getStopsAfter(from);

            for (RouteStop stopC : stopsAfterA) {
                String transferStation = stopC.getStation().getName();
                if (transferStation.equals(to)) continue;

                LocalTime earliestDeparture = getEarliestDeparture(firstSchedule, transferStation);

                for (TrainSchedule secondSchedule : getDirectSchedules(transferStation, to)) {
                    if (isValidTransfer(secondSchedule, transferStation, earliestDeparture)) {
                        results.add(List.of(firstSchedule, secondSchedule));
                    }
                }
            }
        }

        return results;
    }

    private List<List<TrainSchedule>> findRoutesWithTwoTransfers(String from, String to) {
        List<List<TrainSchedule>> results = new ArrayList<>();

        for (TrainSchedule firstSchedule : getSchedulesPassingThrough(from)) {
            List<RouteStop> stopsAfterA = firstSchedule.getRoute().getStopsAfter(from);

            for (RouteStop stopC : stopsAfterA) {
                String transferStationC = stopC.getStation().getName();
                if (transferStationC.equals(to)) continue;

                LocalTime earliestDepartureC = getEarliestDeparture(firstSchedule, transferStationC);

                for (TrainSchedule secondSchedule : getSchedulesPassingThrough(transferStationC)) {
                    if (!isValidTransfer(secondSchedule, transferStationC, earliestDepartureC)) continue;

                    List<RouteStop> stopsAfterC = secondSchedule.getRoute().getStopsAfter(transferStationC);

                    for (RouteStop stopD : stopsAfterC) {
                        String transferStationD = stopD.getStation().getName();
                        if (transferStationD.equals(to) || transferStationD.equals(from)) continue;

                        LocalTime earliestDepartureD = getEarliestDeparture(secondSchedule, transferStationD);

                        for (TrainSchedule thirdSchedule : getDirectSchedules(transferStationD, to)) {
                            if (isValidTransfer(thirdSchedule, transferStationD, earliestDepartureD)) {
                                results.add(List.of(firstSchedule, secondSchedule, thirdSchedule));
                            }
                        }
                    }
                }
            }
        }

        return results;
    }

    private List<TrainSchedule> getDirectSchedules(String from, String to) {
        List<Route> validRoutes = routeRepository.findValidRoutesBetweenStations(from, to);
        return trainScheduleRepository.findByRouteIn(validRoutes);
    }

    private List<TrainSchedule> getSchedulesPassingThrough(String stationName) {
        List<Route> routes = routeRepository.findRoutesPassingThroughStation(stationName);
        return trainScheduleRepository.findByRouteIn(routes);
    }

    private LocalTime getEarliestDeparture(TrainSchedule schedule, String station) {
        return schedule.getArrivalTimeAt(station).plusMinutes(MIN_TRANSFER_MINUTES);
    }

    private boolean isValidTransfer(TrainSchedule nextSchedule, String station, LocalTime earliestDeparture) {
        LocalTime departure = nextSchedule.getDepartureTimeAt(station);
        return !departure.isBefore(earliestDeparture);
    }
}