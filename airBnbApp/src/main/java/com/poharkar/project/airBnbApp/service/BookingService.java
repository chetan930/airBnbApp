package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.BookingDto;
import com.poharkar.project.airBnbApp.dto.BookingRequest;
import com.poharkar.project.airBnbApp.dto.GuestDto;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface BookingService {
    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
