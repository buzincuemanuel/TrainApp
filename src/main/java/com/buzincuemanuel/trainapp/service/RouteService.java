package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.Route;
import com.buzincuemanuel.trainapp.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public Route save(Route route) {
        return routeRepository.save(route);
    }

    public Route update(Long id, Route route) {
        route.setId(id);
        return routeRepository.save(route);
    }

    public void deleteById(Long id) {
        routeRepository.deleteById(id);
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public Optional<Route> findById(Long id) {
        return routeRepository.findById(id);
    }
}