package com.buzincuemanuel.trainapp.repository;

import com.buzincuemanuel.trainapp.model.Route;
import com.buzincuemanuel.trainapp.model.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, Long>{

    List<TrainSchedule> findByRouteIn(List<Route> routes);
}