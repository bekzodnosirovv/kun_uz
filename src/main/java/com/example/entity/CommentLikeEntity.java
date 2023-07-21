package com.example.entity;

import com.example.enums.LikeStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.sql.ast.tree.predicate.LikePredicate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comment_like")
public class CommentLikeEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity profileId;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentEntity commentId;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private LikeStatus status;
}
