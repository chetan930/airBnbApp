package com.poharkar.project.airBnbApp.dto;

import com.poharkar.project.airBnbApp.entity.enums.Gender;
import com.poharkar.project.airBnbApp.entity.enums.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private Set<Role> roles;
    private Gender gender;
    private LocalDate dateOfBirth;
}
