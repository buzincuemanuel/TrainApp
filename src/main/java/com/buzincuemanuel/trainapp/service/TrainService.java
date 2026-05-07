package com.buzincuemanuel.trainapp.service;


import com.buzincuemanuel.trainapp.model.Train;
import com.buzincuemanuel.trainapp.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;

    public Train save(Train train){
        return trainRepository.save(train);
    }

    public Train update(Long id, Train train) {
        train.setId(id);
        return trainRepository.save(train);
    }

    public void deleteById(Long id) {
        trainRepository.deleteById(id);
    }

    public List<Train> findAll() {
        return trainRepository.findAll();
    }

    public Optional<Train> findById(Long id) {
        return trainRepository.findById(id);
    }
}
