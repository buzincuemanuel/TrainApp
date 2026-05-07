package com.buzincuemanuel.trainapp.repository;

import com.buzincuemanuel.trainapp.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
}