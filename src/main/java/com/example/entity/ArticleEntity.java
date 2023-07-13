package com.example.entity;

import com.example.enums.ArticleStatus;

import java.time.LocalDateTime;

public class ArticleEntity {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer shared_count;
    private Integer image_id;
    private Integer region_id;
    private Integer category_id;
    private Integer moderator_id;
    private Integer publisher_id;
    private ArticleStatus status;
    private LocalDateTime created_date;
    private LocalDateTime published_date;
    private Boolean visible;
    private Integer view_count;
}
