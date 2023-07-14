package com.example.dto;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import lombok.Data;

@Data
public class CreateProfileAdminDTO {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private ProfileStatus status;
    private ProfileRole role;
}
