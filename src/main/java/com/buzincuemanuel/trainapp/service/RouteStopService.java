package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.RouteStop;
import com.buzincuemanuel.trainapp.repository.RouteStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteStopService {

    private final RouteStopRepository routeStopRepository;

    public RouteStop save(RouteStop routeStop) {
        return routeStopRepository.save(routeStop);
    }

    public RouteStop update(Long id, RouteStop routeStop) {
        routeStop.setId(id);
        return routeStopRepository.save(routeStop);
    }

    public void deleteById(Long id) {
        routeStopRepository.deleteById(id);
    }

    public List<RouteStop> findAll() {
        return routeStopRepository.findAll();
    }

    public Optional<RouteStop> findById(Long id) {
        return routeStopRepository.findById(id);
    }
}