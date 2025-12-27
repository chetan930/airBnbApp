package com.poharkar.project.airBnbApp.repository;

import com.poharkar.project.airBnbApp.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}