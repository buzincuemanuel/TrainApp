package com.buzincuemanuel.trainapp.repository;

import com.buzincuemanuel.trainapp.model.Booking;
import com.buzincuemanuel.trainapp.model.TrainSchedule;
import com.buzincuemanuel.trainapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTrainSchedule(TrainSchedule trainSchedule);

    List<Booking> findByUser(User user);

    @Query("SELECT COALESCE(SUM(b.numberOfSeats), 0) FROM Booking b WHERE b.trainSchedule = :schedule")
    Integer sumSeatsBookedForSchedule(@Param("schedule") TrainSchedule schedule);
}