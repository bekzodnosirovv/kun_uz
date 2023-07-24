package com.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleShortInfoDTO {
    private String id;
    private String title;
    private String description;
    private String imageId;
    private String imageURL;
    private LocalDateTime publishedDate;

    public ArticleShortInfoDTO(String id, String title, String description,
                               String imageId, String imageURL, LocalDateTime publishedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
        this.imageURL = imageURL;
        this.publishedDate = publishedDate;
    }

    public ArticleShortInfoDTO() {
    }
}
