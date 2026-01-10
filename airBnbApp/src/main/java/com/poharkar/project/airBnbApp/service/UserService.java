package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.ProfileUpdateRequestDto;
import com.poharkar.project.airBnbApp.dto.UserDto;
import com.poharkar.project.airBnbApp.entity.User;
import org.jspecify.annotations.Nullable;

public interface UserService {
    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
