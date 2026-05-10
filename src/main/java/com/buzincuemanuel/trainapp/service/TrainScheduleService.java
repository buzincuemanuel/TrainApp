package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.TrainSchedule;
import com.buzincuemanuel.trainapp.repository.TrainScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainScheduleService {

    private final TrainScheduleRepository trainScheduleRepository;
    private final NotificationService notificationService;

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

    @Transactional
    public void updateDelay(Long id, Integer delayMinutes) {
        TrainSchedule schedule = trainScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id: " + id));
        schedule.setDelayMinutes(delayMinutes);
        trainScheduleRepository.save(schedule);
        if (delayMinutes > 0) {
            try {
                notificationService.sendDelayNotification(schedule, delayMinutes);
            } catch (Exception e) {
                System.err.println("Could not send delay emails: " + e.getMessage());
            }
        }
    }
}