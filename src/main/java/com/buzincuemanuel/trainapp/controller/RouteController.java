package com.buzincuemanuel.trainapp.controller;

import com.buzincuemanuel.trainapp.dto.RouteResultDto;
import com.buzincuemanuel.trainapp.dto.TrainLegDto;
import com.buzincuemanuel.trainapp.model.TrainSchedule;
import com.buzincuemanuel.trainapp.service.RouteFinderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class RouteController {

    private final RouteFinderService routeFinderService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    public String searchRoutes(@RequestParam(required = false) String from,
                               @RequestParam(required = false) String to,
                               Model model) {

        if (from == null || to == null || from.isBlank() || to.isBlank()) {
            return "search";
        }

        List<List<TrainSchedule>> journeys = routeFinderService.findAllRoutes(from, to);

        List<RouteResultDto> results = journeys.stream()
                .map(journey -> convertToDto(journey, from, to))
                .collect(Collectors.toList());

        model.addAttribute("results", results);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "search-results";
    }

    private RouteResultDto convertToDto(List<TrainSchedule> journey, String from, String to) {
        List<TrainLegDto> legs = new ArrayList<>();
        String currentStart = from;

        for (int i = 0; i < journey.size(); i++) {
            TrainSchedule schedule = journey.get(i);
            String currentEnd;

            if (i == journey.size() - 1) {
                currentEnd = to;
            } else {
                TrainSchedule nextSchedule = journey.get(i + 1);
                currentEnd = findTransferStation(schedule, nextSchedule, currentStart, to);
            }

            legs.add(TrainLegDto.builder()
                    .scheduleId(schedule.getId())
                    .trainName(schedule.getTrain().getName())
                    .fromStation(currentStart)
                    .toStation(currentEnd)
                    .departureTime(schedule.getDepartureTimeAt(currentStart))
                    .arrivalTime(schedule.getArrivalTimeAt(currentEnd))
                    .delayMinutes(schedule.getDelayMinutes())
                    .build());

            currentStart = currentEnd;
        }

        return RouteResultDto.builder()
                .departureStation(from)
                .arrivalStation(to)
                .totalDepartureTime(legs.get(0).getDepartureTime())
                .totalArrivalTime(legs.get(legs.size() - 1).getArrivalTime())
                .numberOfTransfers(journey.size() - 1)
                .legs(legs)
                .build();
    }

    private String findTransferStation(TrainSchedule current, TrainSchedule next, String from, String to) {
        List<String> currentRouteStations = current.getRoute().getStopsAfter(from).stream()
                .map(stop -> stop.getStation().getName())
                .collect(Collectors.toList());
        for (String station : currentRouteStations) {
            if (next.getRoute().containsStation(station) && next.getRoute().stationComesAfter(station, to)) {
                return station;
            }
        }
        return to;
    }
}