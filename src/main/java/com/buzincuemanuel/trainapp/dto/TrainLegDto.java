package com.buzincuemanuel.trainapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class TrainLegDto {
    private Long scheduleId;
    private String trainName;
    private String fromStation;
    private String toStation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int delayMinutes;
}