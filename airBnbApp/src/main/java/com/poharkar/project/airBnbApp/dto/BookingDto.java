package com.poharkar.project.airBnbApp.dto;

import com.poharkar.project.airBnbApp.entity.Hotel;
import com.poharkar.project.airBnbApp.entity.Room;
import com.poharkar.project.airBnbApp.entity.User;
import com.poharkar.project.airBnbApp.entity.enums.BookingStatus;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {
    private Long id;

//    private Hotel hotel;

//    private Room room;

//    private User user;

    private Integer roomsCount;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BookingStatus bookingStatus;

    private Set<GuestDto> guests;

    private BigDecimal amount;

}
