package com.poharkar.project.airBnbApp.controller;

import com.poharkar.project.airBnbApp.dto.HotelDto;
import com.poharkar.project.airBnbApp.dto.HotelInfoDto;
import com.poharkar.project.airBnbApp.dto.HotelSearchRequest;
import com.poharkar.project.airBnbApp.service.HotelService;
import com.poharkar.project.airBnbApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest){
        log.info("Attempting to fetch a hotel with HotelSearchRequest {}", hotelSearchRequest.getCity());
        Page<HotelDto> page=inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        log.info("Attempting to fetch a hotel info with Hotel ID {}", hotelId);
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}
