package com.example.superEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
@MappedSuperclass
public class BaseEntity extends SuperEntity {
    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;
}