package com.poharkar.project.airBnbApp.repository;

import com.poharkar.project.airBnbApp.entity.Hotel;
import com.poharkar.project.airBnbApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findByOwner(User user);
}
