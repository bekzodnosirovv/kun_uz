package com.example.util;

import com.example.dto.ProfileDTO;
import com.example.exp.AppBadRequestException;
import org.springframework.stereotype.Component;

@Component
public class ProfileValidateCheckUtil {

    public void profileCheck(ProfileDTO dto) {
        if (dto.getName() == null) throw new AppBadRequestException("Name not found.");
        if (dto.getSurname() == null) throw new AppBadRequestException("Surname not found.");
        if (dto.getEmail() == null) throw new AppBadRequestException("Email not found.");
        if (!dto.getEmail().endsWith("@gmail.com")) throw new AppBadRequestException("Email is invalid");
        if (dto.getPhone() == null) throw new AppBadRequestException("Phone not found.");
        if (!checkPhone(dto.getPhone())) throw new AppBadRequestException("Phone number is invalid.");
        if (dto.getPassword() == null) throw new AppBadRequestException("Password not found.");
        if (dto.getStatus() == null) throw new AppBadRequestException("Status not found.");
        if (dto.getRole() == null) throw new AppBadRequestException("Profile role not found.");
    }

    private boolean checkPhone(String phone) {
        if (!phone.startsWith("+998") || phone.length() != 13) return false;
        return phone.substring(2).chars().allMatch(Character::isDigit);
    }
}
