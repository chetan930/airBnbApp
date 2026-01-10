package com.poharkar.project.airBnbApp.security;

import com.poharkar.project.airBnbApp.dto.LoginDto;
import com.poharkar.project.airBnbApp.dto.SignUpRequestDto;
import com.poharkar.project.airBnbApp.dto.UserDto;
import com.poharkar.project.airBnbApp.entity.User;
import com.poharkar.project.airBnbApp.entity.enums.Role;
import com.poharkar.project.airBnbApp.exception.ResourceNotFoundException;
import com.poharkar.project.airBnbApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserDto signUp(SignUpRequestDto signUpRequestDto){
        User user =userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);

        if(user!=null){
            throw new RuntimeException("User is already present with given email id");
        }

        User newUser=modelMapper.map(signUpRequestDto, User.class);
        newUser.setRoles(Set.of(Role.GUEST));
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        newUser=userRepository.save(newUser);
        return modelMapper.map(newUser,UserDto.class);

    }

    public String[] login(LoginDto loginDto){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),loginDto.getPassword()
        ));

        User user= (User) authentication.getPrincipal();
        String[] arr=new String[2];
        arr[0]=jwtService.generateAccessToken(user);
        arr[1]=jwtService.generateRefreshToken(user);

        return arr;
    }

    public String refreshToken(String refreshToken){
        Long userId=jwtService.getUserIdFromToken(refreshToken);
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User doest not exist with id: "+userId));
        return jwtService.generateAccessToken(user);
    }
}
