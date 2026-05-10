package com.buzincuemanuel.trainapp.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    @Builder.Default
    private List<RouteStop> stops = new ArrayList<>();

    public boolean containsStation(String stationName) {
        return stops.stream()
                .anyMatch(stop -> stop.getStation().getName().equals(stationName));
    }

    public boolean stationComesAfter(String first, String second) {
        int firstOrder = stops.stream()
                .filter(stop -> stop.getStation().getName().equals(first))
                .mapToInt(RouteStop::getStopOrder)
                .findFirst()
                .orElse(-1);

        int secondOrder = stops.stream()
                .filter(stop -> stop.getStation().getName().equals(second))
                .mapToInt(RouteStop::getStopOrder)
                .findFirst()
                .orElse(-1);

        return firstOrder != -1 && secondOrder != -1 && secondOrder > firstOrder;
    }

    public List<RouteStop> getStopsAfter(String stationName) {
        int stationOrder = stops.stream()
                .filter(stop -> stop.getStation().getName().equals(stationName))
                .mapToInt(RouteStop::getStopOrder)
                .findFirst()
                .orElse(-1);

        if (stationOrder == -1) {
            return List.of();
        }

        return stops.stream()
                .filter(stop -> stop.getStopOrder() > stationOrder)
                .sorted(Comparator.comparingInt(RouteStop::getStopOrder))
                .collect(Collectors.toList());
    }
}
