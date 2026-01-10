package com.poharkar.project.airBnbApp.controller;

import com.poharkar.project.airBnbApp.dto.BookingDto;
import com.poharkar.project.airBnbApp.dto.HotelDto;
import com.poharkar.project.airBnbApp.dto.HotelReportDto;
import com.poharkar.project.airBnbApp.service.BookingService;
import com.poharkar.project.airBnbApp.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
    private final HotelService hotelService;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto){
        log.info("Attempting to create a new hotel with name {}", hotelDto.getName());
        return new ResponseEntity<>(hotelService.createNewHotel(hotelDto), HttpStatus.CREATED);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId){
        log.info("Attempting to fetch a hotel with ID {}", hotelId);
        return ResponseEntity.ok(hotelService.getHotelById(hotelId));
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long hotelId,@RequestBody HotelDto hotelDto){
        log.info("Attempting to update a hotel with ID {}", hotelId);
        return ResponseEntity.ok(hotelService.updateHotelById(hotelId,hotelDto));
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId){
        log.info("Attempting to delete a hotel with ID {}", hotelId);
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<Void> activateHotelById(@PathVariable Long hotelId){
        log.info("Attempting to activate hotel with ID {}", hotelId);
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<HotelDto>> getAllHotels(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<List<BookingDto>> getAllBookingsByHotelId(@PathVariable Long hotelId){
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/report")
    public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId,
                                                         @RequestParam(required = false)LocalDate startDate,
                                                         @RequestParam(required = false)LocalDate endDate){
        if(startDate==null) startDate=LocalDate.now().minusMonths(1);
        if(endDate==null) endDate=LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId,startDate,endDate));
    }
}
