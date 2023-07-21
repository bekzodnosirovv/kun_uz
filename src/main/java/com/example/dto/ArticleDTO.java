package com.example.dto;

import com.example.entity.AttachEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ArticleStatus;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ArticleDTO {

    private String id;
    private String title;
    private String description;
    private String content;
    private Integer sharedCount;
    private AttachEntity imageId;
    private RegionEntity regionId;
    private CategoryEntity categoryId;
    private ProfileEntity moderatorId;
    private ProfileEntity publisherId;
    private ArticleStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private boolean visible;
    private Integer viewCount;


}
