package com.buzincuemanuel.trainapp.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "train_schedules")
public class TrainSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private Integer delayMinutes = 0;

    @OneToMany(mappedBy = "trainSchedule")
    private List<Booking> bookings = new ArrayList<>();

    public LocalTime getArrivalTimeAt(String stationName) {
        return route.getStops().stream()
                .filter(stop -> stop.getStation().getName().equals(stationName))
                .findFirst()
                .map(stop -> departureTime
                        .plusMinutes(stop.getMinutesFromStart())
                        .plusMinutes(delayMinutes))
                .orElseThrow(() -> new RuntimeException("Station " + stationName + " not found in route"));
    }

    public LocalTime getDepartureTimeAt(String stationName) {
        return getArrivalTimeAt(stationName);
    }
}
