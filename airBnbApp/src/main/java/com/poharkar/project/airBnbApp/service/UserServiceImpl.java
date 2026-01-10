package com.poharkar.project.airBnbApp.service;

import com.poharkar.project.airBnbApp.dto.ProfileUpdateRequestDto;
import com.poharkar.project.airBnbApp.dto.UserDto;
import com.poharkar.project.airBnbApp.entity.User;
import com.poharkar.project.airBnbApp.exception.ResourceNotFoundException;
import com.poharkar.project.airBnbApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.poharkar.project.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User does not exist with id: "+id));
    }

    @Override
    public void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user=getCurrentUser();

        if(profileUpdateRequestDto.getDateOfBirth()!=null) user.setDateOfBirth(profileUpdateRequestDto.getDateOfBirth());
        if(profileUpdateRequestDto.getGender()!=null) user.setGender(profileUpdateRequestDto.getGender());
        if(profileUpdateRequestDto.getName()!=null) user.setName(profileUpdateRequestDto.getName());

        userRepository.save(user);

    }

    @Override
    public UserDto getMyProfile() {
        User user=getCurrentUser();
        log.info("Getting the profile for user with Id: {}",user.getId());
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }
}
