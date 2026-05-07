package com.buzincuemanuel.trainapp.repository;

import com.buzincuemanuel.trainapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}