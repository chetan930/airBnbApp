package com.poharkar.project.airBnbApp.repository;

import com.poharkar.project.airBnbApp.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
}
