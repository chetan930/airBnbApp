package com.poharkar.project.airBnbApp.controller;

import com.poharkar.project.airBnbApp.dto.BookingDto;
import com.poharkar.project.airBnbApp.dto.GuestDto;
import com.poharkar.project.airBnbApp.dto.ProfileUpdateRequestDto;
import com.poharkar.project.airBnbApp.dto.UserDto;
import com.poharkar.project.airBnbApp.service.BookingService;
import com.poharkar.project.airBnbApp.service.GuestService;
import com.poharkar.project.airBnbApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    private final GuestService guestService;

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequestDto profileUpdateRequestDto){
        userService.updateProfile(profileUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getMyProfile(){
        return ResponseEntity.ok(userService.getMyProfile());
    }


    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingDto>> getMyBookings(){
        return ResponseEntity.ok(bookingService.getMyBookings());
    }



    @GetMapping("/guests")
    @Operation(summary = "Get all my guests", tags = {"Booking Guests"})
    public ResponseEntity<List<GuestDto>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @PostMapping("/guests")
    @Operation(summary = "Add a new guest to my guests list", tags = {"Booking Guests"})
    public ResponseEntity<GuestDto> addNewGuest(@RequestBody GuestDto guestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.addNewGuest(guestDto));
    }

    @PutMapping("guests/{guestId}")
    @Operation(summary = "Update a guest", tags = {"Booking Guests"})
    public ResponseEntity<Void> updateGuest(@PathVariable Long guestId, @RequestBody GuestDto guestDto) {
        guestService.updateGuest(guestId, guestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("guests/{guestId}")
    @Operation(summary = "Remove a guest", tags = {"Booking Guests"})
    public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }
}
