package com.poharkar.project.airBnbApp.repository;

import com.poharkar.project.airBnbApp.entity.Guest;
import com.poharkar.project.airBnbApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByUser(User user);
}