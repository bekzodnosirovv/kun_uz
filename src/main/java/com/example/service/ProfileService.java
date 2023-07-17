package com.example.service;

import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.PhoneIsValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Arrays;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PhoneIsValid phoneIsValid;


    public ProfileDTO create(ProfileDTO dto) {
        isValidProfile(dto);
        if (profileRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ItemNotFoundException("Email already exists.");
        }
        if (profileRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new ItemNotFoundException("Phone already exists.");
        }

        ProfileEntity entity = toEntity(dto);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);

        profileRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;

    }

    public void update(ProfileDTO dto) {


    }

    public void updateProfileDetail() {
    }

    public void getAll() {
    }

    public void delete() {
    }

    public void updatePhoto() {
    }

    public void filter() {
    }

    private void toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());


    }

    public ProfileEntity toEntity(ProfileDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());
        entity.setStatus(dto.getStatus());
        entity.setRole(dto.getRole());

        return entity;
    }

    private void isValidProfile(ProfileDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new AppBadRequestException("Name required.");
        }
        if (dto.getSurname() == null || dto.getSurname().isBlank()) {
            throw new AppBadRequestException("Surname required.");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new AppBadRequestException("Email not found.");
        }
        if (dto.getPhone() == null || dto.getPhone().isBlank()) {
            throw new AppBadRequestException("Phone required.");
        }
        if (!phoneIsValid.checkPhone(dto.getPhone())) {
            throw new AppBadRequestException("Phone number is invalid.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new AppBadRequestException("Password not found.");
        }
        if (dto.getRole() == null) {
            throw new AppBadRequestException("Profile role required.");
        }
        if (Arrays.stream(ProfileRole.values()).noneMatch(t -> t.equals(dto.getRole()))){
            throw new AppBadRequestException("Profile role not found.");
        }


    }


}


