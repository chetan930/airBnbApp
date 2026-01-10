package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.*;
import com.poharkar.project.airBnbApp.entity.Hotel;
import com.poharkar.project.airBnbApp.entity.Inventory;
import com.poharkar.project.airBnbApp.entity.Room;
import com.poharkar.project.airBnbApp.entity.User;
import com.poharkar.project.airBnbApp.exception.ResourceNotFoundException;
import com.poharkar.project.airBnbApp.repository.HotelMinPriceRepository;
import com.poharkar.project.airBnbApp.repository.InventoryRepository;
import com.poharkar.project.airBnbApp.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.poharkar.project.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService{
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final ModelMapper modelMapper;


    @Override
    public void initializeRoomForAYear(Room room) {

        LocalDate today=LocalDate.now();
        LocalDate endDate=today.plusYears(1);
        for(;!today.isAfter(endDate);today=today.plusDays(1)){
            Inventory inventory=Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteInventories(Room room) {
        log.info("Deleting inventory of room with ID: {} ",room.getId());
//        LocalDate today=LocalDate.now();
        inventoryRepository.deleteByRoom(room);
    }

//    @Override
//    public Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
//        log.info("Searching hotel for {} city, from {} to {}", hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());
//        Pageable pageable= PageRequest.of(hotelSearchRequest.getPage(),hotelSearchRequest.getSize());
//
//        Long dateCount= ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate())+1;
//
//        Page<Hotel> hotelPage=inventoryRepository.findHotelsWithAvailableInventory(hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),
//                hotelSearchRequest.getEndDate(),hotelSearchRequest.getRoomsCount(),dateCount,pageable);
//
//        return hotelPage.map((element)->modelMapper.map(element,HotelDto.class));
//    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotel for {} city, from {} to {}", hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());
        Pageable pageable= PageRequest.of(hotelSearchRequest.getPage(),hotelSearchRequest.getSize());

        Long dateCount= ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate())+1;

        Page<HotelPriceDto> hotelPage=hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),hotelSearchRequest.getRoomsCount(),dateCount,pageable);

        return hotelPage;
    }

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        log.info("Getting all inventory by room with room id: {}",roomId);
        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->new ResourceNotFoundException("Room not found with id: "+roomId));

        User user=getCurrentUser();
        if(!user.getId().equals(room.getHotel().getOwner().getId())){
            throw new AccessDeniedException("You are not owner of room with id: "+roomId);
        }

        List<Inventory> inventories=inventoryRepository.findByRoomOrderByDate(room);
        return inventories
                .stream()
                .map(inventory -> modelMapper.map(inventory,InventoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("Updating inventory by room with room id: {} between date range: {} - {}",roomId,updateInventoryRequestDto.getStartDate(),updateInventoryRequestDto.getEndDate());
        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->new ResourceNotFoundException("Room not found with id: "+roomId));

        User user=getCurrentUser();
        if(!user.getId().equals(room.getHotel().getOwner().getId())){
            throw new AccessDeniedException("You are not owner of room with id: "+roomId);
        }

        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId,updateInventoryRequestDto.getStartDate(),updateInventoryRequestDto.getEndDate());

        inventoryRepository.updateInventory(roomId,updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate(),
                updateInventoryRequestDto.getClosed(),
                updateInventoryRequestDto.getSurgeFactor());
    }
}
