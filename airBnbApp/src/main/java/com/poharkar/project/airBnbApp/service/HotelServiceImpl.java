package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.HotelDto;
import com.poharkar.project.airBnbApp.dto.HotelInfoDto;
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
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.poharkar.project.airBnbApp.util.AppUtils.getCurrentUser;

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

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        Hotel savedHotel=hotelRepository.save(hotel);
        log.info("Created a new hotel with ID {}", savedHotel.getId());

        return modelMapper.map(savedHotel,HotelDto.class);
    }

//    just for dumping bulk data
    @Override
    public List<HotelDto> createBulkHotels(List<HotelDto> hotelDtoList) {

        List<Hotel> hotelList= hotelDtoList.stream()
                .map((element) -> modelMapper.map(element, Hotel.class))
                .toList();

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Hotel> savedHotelList=new ArrayList<>();

        for(Hotel hotel : hotelList){
            log.info("Creating a new hotel with name {}", hotel.getName());
            hotel.setActive(true);
            hotel.setOwner(user);
            Hotel savedHotel=hotelRepository.save(hotel);
            savedHotelList.add(savedHotel);
            log.info("Created a new hotel with ID {}", savedHotel.getId());
        }


        return savedHotelList.stream()
                .map((element) -> modelMapper.map(element, HotelDto.class))
                .toList();
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting a hotel with ID {}", id);
        Hotel hotel=hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+id));

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.getId().equals(hotel.getOwner().getId())){
            throw new UnAuthorisedException("This user does not own the hotel with id "+id);
        }

        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {

        log.info("Updating a hotel with ID {}", id);
        Hotel hotel=hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with ID "+id));

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id "+id);
        }

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

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id "+id);
        }

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

        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorisedException("This user does not own the hotel with id "+hotelId);
        }

        hotel.setActive(true);

        //assuming only do it once
        for(Room room: hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
    }


    //public method
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

    @Override
    public List<HotelDto> getAllHotels() {
        User user=getCurrentUser();
        log.info("Getting all hotels for the admin user with ID: {}",user.getId());
        List<Hotel> hotels=hotelRepository.findByOwner(user);
        return hotels
                .stream()
                .map(hotel -> modelMapper.map(hotel,HotelDto.class))
                .collect(Collectors.toList());
    }
}
