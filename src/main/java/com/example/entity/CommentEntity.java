package com.example.entity;

import java.time.LocalDateTime;

public class CommentEntity {

    private String id;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private Integer profileId;
    private String content;
    private String articleId;
    private Integer replyId;
    private boolean visible;

}
