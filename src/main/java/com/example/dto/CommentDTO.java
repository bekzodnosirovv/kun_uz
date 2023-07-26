package com.example.dto;

import com.example.entity.ArticleEntity;
import com.example.entity.ProfileEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDTO {

    private String id;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private Integer profileId;
    private ProfileDTO profile;
    private String content;
    private String articleId;
    private ArticleDTO article;
    private Boolean visible;
    private String replyId;


}
