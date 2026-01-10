package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.BookingDto;
import com.poharkar.project.airBnbApp.dto.BookingRequest;
import com.poharkar.project.airBnbApp.dto.GuestDto;
import com.poharkar.project.airBnbApp.dto.HotelReportDto;
import com.stripe.model.Event;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayment(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();
}
