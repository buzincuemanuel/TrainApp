package com.buzincuemanuel.trainapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.time.Duration;
import java.util.List;

@Data
@Builder
public class RouteResultDto {
    private String departureStation;
    private String arrivalStation;
    private LocalTime totalDepartureTime;
    private LocalTime totalArrivalTime;
    private int numberOfTransfers;
    private List<TrainLegDto> legs;

    public String getFormattedTotalDuration() {
        if (totalDepartureTime != null && totalArrivalTime != null) {
            Duration duration = Duration.between(totalDepartureTime, totalArrivalTime);
            if (duration.isNegative()) {
                duration = duration.plusDays(1);
            }
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            return hours + "h " + minutes + "m";
        }
        return "-";
    }
}