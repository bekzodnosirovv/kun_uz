package com.example.service;

import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.exp.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.ProfileValidateCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileValidateCheckUtil checkUtil;

    public ProfileDTO create(ProfileDTO dto) {
        checkUtil.profileCheck(dto);
        if (profileRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ItemNotFoundException("This is an email registered");
        }
        if (profileRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new ItemNotFoundException("This phone is registered");
        }
        ProfileEntity entity = toEntity(dto);
        profileRepository.save(entity);
        dto.setVisible(entity.getVisible());
        dto.setCreated_date(entity.getCreated_date());
        dto.setPhoto_id(entity.getPhoto_id());
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


}
