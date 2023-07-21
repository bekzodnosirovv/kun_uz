package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity profileId;
    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity articleId;
    private Integer replyId;
    private boolean visible;

}
