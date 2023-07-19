package com.example.service;

import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.CustomRepository;
import com.example.repository.ProfileRepository;
import com.example.util.MD5Util;
import com.example.util.PhoneIsValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CustomRepository customRepository;


    public ProfileDTO create(Integer id, ProfileDTO dto) {

        isValidProfile(dto);
        if (profileRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ItemNotFoundException("Email already exists.");
        }
        if (profileRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new ItemNotFoundException("Phone already exists.");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);

        profileRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public void update(Integer id, ProfileDTO dto) {
        getById(id);
        ProfileEntity entity = profileRepository.findById(id).get();
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getSurname() != null) entity.setSurname(dto.getSurname());
        if (dto.getVisible() != null) entity.setVisible(dto.getVisible());
        if (dto.getPassword() != null) entity.setPassword(MD5Util.encode(dto.getPassword()));
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getRole() != null) entity.setRole(dto.getRole());

        profileRepository.save(entity);
    }

    public void updateProfileDetail(Integer id, ProfileDTO dto) {
        ProfileDTO updateProfile = getById(id);
        if (dto.getName() != null) updateProfile.setName(dto.getName());
        if (dto.getSurname() != null) updateProfile.setSurname(dto.getSurname());
        profileRepository.updateDetail(id, updateProfile.getName(), updateProfile.getSurname());

    }

    public PageImpl<ProfileDTO> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProfileEntity> entityPage = profileRepository.findAll(pageable);
        return new PageImpl<>(entityPage.getContent().stream().map(this::toDTO).toList(), pageable, entityPage.getTotalElements());
    }

    public void delete(Integer id) {
        getById(id);
        profileRepository.deletedById(id);
    }

    public void updatePhoto() {
    }

    public ProfileDTO getById(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("Profile not found.");
        return toDTO(optional.get());
    }

    public ProfileDTO filter(ProfileFilterDTO filterDTO) {
        List<ProfileEntity> list = (List<ProfileEntity>) customRepository.filterStudent(filterDTO);
        return (ProfileDTO) list.stream().map(this::toDTO).toList();
    }

    private ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setVisible(entity.isVisible());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
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
        if (!PhoneIsValidUtil.checkPhone(dto.getPhone())) {
            throw new AppBadRequestException("Phone number is invalid.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new AppBadRequestException("Password required.");
        }
        if (dto.getRole() == null) {
            throw new AppBadRequestException("Profile role required.");
        }
        if (Arrays.stream(ProfileRole.values()).noneMatch(t -> t.equals(dto.getRole()))) {
            throw new AppBadRequestException("Profile role not found.");
        }


    }


}


