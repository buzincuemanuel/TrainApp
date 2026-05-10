package com.buzincuemanuel.trainapp.repository;

import com.buzincuemanuel.trainapp.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT r FROM Route r " +
            "JOIN r.stops stopA " +
            "JOIN r.stops stopB " +
            "WHERE stopA.station.name = :departureStation " +
            "AND stopB.station.name = :arrivalStation " +
            "AND stopA.stopOrder < stopB.stopOrder")
    List<Route> findValidRoutesBetweenStations(
            @Param("departureStation") String departureStation,
            @Param("arrivalStation") String arrivalStation
    );
    @Query("SELECT r FROM Route r JOIN r.stops rs WHERE rs.station.name = :stationName")
    List<Route> findRoutesPassingThroughStation(@Param("stationName") String stationName);
}