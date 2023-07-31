package com.example.service;

import com.example.dto.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private MailSenderService mailSenderService;

    @Value("${server.url}")
    private String serverUrl;


    public ApiResponseDTO registration(RegDTO dto) {
        // check
        ProfileEntity profile = profileService.getByEmail(dto.getEmail());
        if (profile != null) {
            if (profile.getStatus().equals(ProfileStatus.REGISTRATION)) {
                profileRepository.delete(profile);
            } else return new ApiResponseDTO(false, "Email already exists.");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.REGISTRATION);
        profileRepository.save(entity);
        mailSenderService.sendEmailVerification(entity);
        return new ApiResponseDTO(true, "The verification link was send to email.");
    }

    public ApiResponseDTO emailVerification(String jwt) {
        JwtDTO jwtDTO = JWTUtil.decodeEmailJwt(jwt);
        ProfileEntity entity = profileService.getById(jwtDTO.getId());

        if (!entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            throw new AppBadRequestException("Wrong status");
        }

        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity); // update
        return new ApiResponseDTO(true, "Registration completed");
    }


    public ApiResponseDTO login(AuthDTO authDTO) {
        ProfileEntity entity = profileService.getByPhone(authDTO.getPhone());
        if (entity == null || !entity.getPassword().equals(MD5Util.encode(authDTO.getPassword()))) {
            return new ApiResponseDTO(false, "Login or Password not found");
        }
        if (!entity.getStatus().equals(ProfileStatus.ACTIVE) || !entity.getVisible()) {
            return new ApiResponseDTO(false, "Your status not active. Please contact with support.");
        }
        ProfileDTO response = new ProfileDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setSurname(entity.getSurname());
        response.setRole(entity.getRole());
        response.setPhone(entity.getPhone());
        response.setJwt(JWTUtil.encode(entity.getId(), entity.getRole())); // set jwt token
        // response
        return new ApiResponseDTO(true, response);
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
