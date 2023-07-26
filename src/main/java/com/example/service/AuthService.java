package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import com.example.util.PhoneIsValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileService profileService;

    public ApiResponseDTO login(AuthDTO authDTO) {
        ProfileEntity profileEntity = profileService.getByPhone(authDTO.getPhone());
        if (profileEntity == null || !profileEntity.getPassword().equals(MD5Util.encode(authDTO.getPassword()))) {
            return new ApiResponseDTO(false, "Login or Password not found");
        }
        if (!profileEntity.getStatus().equals(ProfileStatus.ACTIVE) || !profileEntity.getVisible()) {
            return new ApiResponseDTO(false, "Your status not active. Please contact with support.");
        }
        ProfileDTO response = new ProfileDTO();
        response.setId(profileEntity.getId());
        response.setName(profileEntity.getName());
        response.setSurname(profileEntity.getSurname());
        response.setRole(profileEntity.getRole());
        response.setPhone(profileEntity.getPhone());
        response.setJwt(JWTUtil.encode(profileEntity.getId(), profileEntity.getRole())); // set jwt token
        // response
        return new ApiResponseDTO(true, response);
    }

    public ProfileDTO registration(ProfileDTO dto) {
        userIsValid(dto); // check dto
        if (profileService.getByEmail(dto.getEmail()) != null) { // check email
            throw new ItemNotFoundException("Email already exists.");
        }
        if (profileService.getByPhone(dto.getPhone()) != null) { // check phone
            throw new ItemNotFoundException("Phone already exists.");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setPhone(dto.getPhone());
        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity); // save
        // response dto
        dto.setId(entity.getId());
        return dto;
    }

    private void userIsValid(ProfileDTO dto) {
        if (dto.getName() == null) {
            throw new AppBadRequestException("Name required");
        }
        if (dto.getSurname() == null) {
            throw new AppBadRequestException("Surname required");
        }
        if (dto.getEmail() == null) {
            throw new AppBadRequestException("Email required");
        }
        if (dto.getPhone() == null) {
            throw new AppBadRequestException("Phone required");
        }
        if (!PhoneIsValidUtil.checkPhone(dto.getPhone())) {
            throw new AppBadRequestException("Phone number is invalid");
        }
        if (dto.getPassword() == null) {
            throw new AppBadRequestException("Password required");
        }


    }
}
