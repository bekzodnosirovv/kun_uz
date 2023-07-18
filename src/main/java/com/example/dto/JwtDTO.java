package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JwtDTO {
    private Integer id;
    private ProfileRole role;
    private Date expireDate;

    public JwtDTO(Integer id, ProfileRole role, Date expireDate) {
        this.id = id;
        this.role = role;
        this.expireDate = expireDate;

    }
}
