package com.buzincuemanuel.trainapp.repository;

import com.buzincuemanuel.trainapp.model.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {}