package com.example.entity;

import com.example.superEntity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tag")
public class Tag extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}