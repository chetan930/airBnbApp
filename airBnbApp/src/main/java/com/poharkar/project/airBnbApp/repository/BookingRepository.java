package com.poharkar.project.airBnbApp.repository;

import com.poharkar.project.airBnbApp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
