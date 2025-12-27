package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.HotelDto;
import com.poharkar.project.airBnbApp.dto.HotelPriceDto;
import com.poharkar.project.airBnbApp.dto.HotelSearchRequest;
import com.poharkar.project.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
