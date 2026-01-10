package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.RoomDto;
import com.poharkar.project.airBnbApp.entity.Hotel;
import com.poharkar.project.airBnbApp.entity.Room;
import com.poharkar.project.airBnbApp.entity.User;
import com.poharkar.project.airBnbApp.exception.ResourceNotFoundException;
import com.poharkar.project.airBnbApp.exception.UnAuthorisedException;
import com.poharkar.project.airBnbApp.repository.HotelRepository;
import com.poharkar.project.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.poharkar.project.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;


    @Override
    public RoomDto createNewRoom(Long hotelId,RoomDto roomDto) {
        log.info("Creating a new room in hotel with ID {}",hotelId);
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+hotelId));

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id "+hotelId);
        }

        Room room=modelMapper.map(roomDto,Room.class);
        room.setHotel(hotel);
        Room savedRoom=roomRepository.save(room);
        log.info("Created a new room with ID {}", savedRoom.getId());

        if(hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);
        }
        return modelMapper.map(savedRoom,RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomInHotel(Long hotelId) {
        log.info("Getting all rooms in hotel with ID {}",hotelId);
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+hotelId));
        return hotel.getRooms()
                .stream()
                .map((element)->
                        modelMapper.map(element,RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting a room with ID {}",roomId);
        Room room=roomRepository
                .findById(roomId)
                .orElseThrow(()->new ResourceNotFoundException("Room not found with ID "+roomId));
        return modelMapper.map(room,RoomDto.class);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Deleting a room with ID {}",roomId);

        Room room=roomRepository
                .findById(roomId)
                .orElseThrow(()->new ResourceNotFoundException("Room not found with ID "+roomId));

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorisedException("This user does not own the room with id "+room.getId());
        }

        //delete future inventories for this room
        inventoryService.deleteInventories(room);

        roomRepository.deleteById(roomId);



    }

    @Override
    @Transactional
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        log.info("Updating room with ID {}", roomId);
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+hotelId));

        User user=getCurrentUser();
        if(!user.getId().equals(hotel.getOwner().getId())){
            throw new UnAuthorisedException("This user does not own the hotel with id "+hotelId);
        }

        Room room =roomRepository.findById(roomId)
                .orElseThrow(()->new ResourceNotFoundException("Room not found with ID "+roomId));

        modelMapper.map(roomDto,room);
        room.setId(roomId);

        // TODO: if the price or inventory is updated fro this room

        room=roomRepository.save(room);
        return modelMapper.map(room,RoomDto.class);
    }
}
