package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.HotelDto;
import com.poharkar.project.airBnbApp.dto.HotelInfoDto;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId);

    List<HotelDto> getAllHotels();
}
