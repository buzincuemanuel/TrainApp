package com.buzincuemanuel.trainapp.dto;

import lombok.Data;

@Data
public class RouteStopDto {
    private Long stationId;
    private Integer stopOrder;
    private Integer minutesFromStart;
}