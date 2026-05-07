package com.buzincuemanuel.trainapp.service;


import com.buzincuemanuel.trainapp.model.Station;
import com.buzincuemanuel.trainapp.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    public Station save(Station station){
        return stationRepository.save(station);
    }

    public Station update(Long id, Station station) {
        station.setId(id);
        return stationRepository.save(station);
    }

    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    public Optional<Station> findById(Long id) {
        return stationRepository.findById(id);
    }
}
