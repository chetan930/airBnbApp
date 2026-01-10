package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.RoomDto;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface RoomService {

    RoomDto createNewRoom(Long hotelId,RoomDto roomDto);

    List<RoomDto> getAllRoomInHotel(Long hotelId);

    RoomDto getRoomById(Long roomId);

    void deleteRoomById(Long roomId);

    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);
}
