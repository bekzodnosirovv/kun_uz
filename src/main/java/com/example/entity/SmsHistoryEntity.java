package com.example.entity;

import com.example.enums.SmsStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sms_history")
public class SmsHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String message;
    private SmsStatus status;


}
