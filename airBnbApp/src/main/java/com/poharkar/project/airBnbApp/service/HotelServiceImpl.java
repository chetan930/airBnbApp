package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.HotelDto;
import com.poharkar.project.airBnbApp.dto.HotelInfoDto;
import com.poharkar.project.airBnbApp.dto.RoomDto;
import com.poharkar.project.airBnbApp.entity.Hotel;
import com.poharkar.project.airBnbApp.entity.Room;
import com.poharkar.project.airBnbApp.exception.ResourceNotFoundException;
import com.poharkar.project.airBnbApp.repository.HotelRepository;
import com.poharkar.project.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name {}", hotelDto.getName());
        Hotel hotel=modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false); //initially hotel will be inactive, we can use another api to make hotel active
        Hotel savedHotel=hotelRepository.save(hotel);
        log.info("Created a new hotel with ID {}", savedHotel.getId());

        return modelMapper.map(savedHotel,HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting a hotel with ID {}", id);
        Hotel hotel=hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+id));
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {

        log.info("Updating a hotel with ID {}", id);
        Hotel hotel=hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+id));

        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        Hotel updatedHotel=hotelRepository.save(hotel);
        log.info("Updated a new hotel with ID {}", updatedHotel.getId());

        return modelMapper.map(updatedHotel,HotelDto.class);
    }

    @Transactional
    @Override
    public void deleteHotelById(Long id) {

        log.info("Deleting a hotel with ID {}", id);
        Hotel hotel=hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+id));

        //delete future inventories for this hotel
        for(Room room:hotel.getRooms()){
            inventoryService.deleteInventories(room);
            roomRepository.deleteById(room.getId());
        }

        hotelRepository.deleteById(id);

    }

    @Transactional
    @Override
    public void activateHotel(Long hotelId) {

        log.info("Activating a hotel with ID {}", hotelId);
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+hotelId));
        hotel.setActive(true);

        //assuming only do it once
        for(Room room: hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
    }

    @Override
    public @Nullable HotelInfoDto getHotelInfoById(Long hotelId) {

        log.info("Getting Hotel info a hotel with ID {}", hotelId);
        Hotel hotel=hotelRepository
                .findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+hotelId));
        List<RoomDto> rooms= hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .toList();
        return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class),rooms);
    }
}
