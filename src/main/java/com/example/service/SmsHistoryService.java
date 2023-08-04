package com.example.service;

import com.example.entity.SmsHistoryEntity;
import com.example.enums.SmsStatus;
import com.example.exp.AppBadRequestException;
import com.example.repository.SmsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsHistoryService {
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;

    public void save(String phone, String message) {
        long smsCount = smsHistoryRepository.countByPhoneAndCreatedDateAfter(phone, LocalDateTime.now().minusMinutes(1));
        if (smsCount >= 4) {
            throw new AppBadRequestException("Please try again in 1 minute");
        }
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setPhone(phone);
        entity.setMessage(message);
        entity.setStatus(SmsStatus.NEW);
        smsHistoryRepository.save(entity);
    }

}
