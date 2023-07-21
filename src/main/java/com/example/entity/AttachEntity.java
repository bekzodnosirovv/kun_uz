package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity()
@Table(name = "attach")
public class AttachEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "original_name")
    private String originalName;
    @Column(name = "path", nullable = false)
    private String path;
    @Column(name = "size", nullable = false)
    private Long size;
    @Column(name = "extension", nullable = false)
    private String extension;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
}
