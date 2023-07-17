package com.example.util;

import org.springframework.stereotype.Component;

@Component
public class PhoneIsValid {

    public boolean checkPhone(String phone) {
        if (!phone.startsWith("+998") || phone.length() != 13) return false;
        return phone.substring(2).chars().allMatch(Character::isDigit);
    }
}
