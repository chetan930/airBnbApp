package com.poharkar.project.airBnbApp.controller;

import com.poharkar.project.airBnbApp.dto.HotelDto;
import com.poharkar.project.airBnbApp.dto.RoomDto;
import com.poharkar.project.airBnbApp.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
@Slf4j
public class RoomAdminController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoomInHotel(@PathVariable Long hotelId,@RequestBody RoomDto roomDto){
        log.info("Attempting to create a new room in hotel with hotelId {}", hotelId);
        return new ResponseEntity<>(roomService.createNewRoom(hotelId,roomDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId){
        log.info("Attempting to get all room in hotel with hotelId {}", hotelId);
        return ResponseEntity.ok(roomService.getAllRoomInHotel(hotelId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId){
        log.info("Attempting to get a new room in hotel with roomId {}", roomId);
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }


    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long roomId){
        log.info("Attempting to delete a new room in hotel with roomId {}", roomId);
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }
}
