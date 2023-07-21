package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "saved_article")
public class SavedArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity profileId;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity articleId;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
