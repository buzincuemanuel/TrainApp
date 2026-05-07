package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.TrainSchedule;
import com.buzincuemanuel.trainapp.repository.TrainScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainScheduleService {

    private final TrainScheduleRepository trainScheduleRepository;

    public TrainSchedule save(TrainSchedule trainSchedule) {
        return trainScheduleRepository.save(trainSchedule);
    }

    public TrainSchedule update(Long id, TrainSchedule trainSchedule) {
        trainSchedule.setId(id);
        return trainScheduleRepository.save(trainSchedule);
    }

    public void deleteById(Long id) {
        trainScheduleRepository.deleteById(id);
    }

    public List<TrainSchedule> findAll() {
        return trainScheduleRepository.findAll();
    }

    public Optional<TrainSchedule> findById(Long id) {
        return trainScheduleRepository.findById(id);
    }
}