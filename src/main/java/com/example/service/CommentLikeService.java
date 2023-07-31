package com.example.service;

import com.example.dto.CommentLikeDTO;
import com.example.entity.CommentLikeEntity;
import com.example.repository.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentLikeService {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public boolean like(Integer profileId, CommentLikeDTO dto) {
        // check
        CommentLikeEntity entity = get(profileId, dto.getCommentId());
        if (entity != null) {
            remove(profileId, dto.getCommentId());
        }
        entity = new CommentLikeEntity();
        entity.setProfileId(profileId);
        entity.setCommentId(dto.getCommentId());
        entity.setStatus(dto.getStatus());
        commentLikeRepository.save(entity);

        return true;
    }

    public boolean dislike(Integer profileId, CommentLikeDTO dto) {
        // check

        CommentLikeEntity entity = get(profileId, dto.getCommentId());
        if (entity != null) {
            remove(profileId, dto.getCommentId());
        }

        entity = new CommentLikeEntity();
        entity.setProfileId(profileId);
        entity.setCommentId(dto.getCommentId());
        entity.setStatus(dto.getStatus());
        commentLikeRepository.save(entity);

        return true;
    }

    public boolean remove(Integer profileId, String commentId) {
        if (profileId == null || commentId == null) return false;
        int effectRow = commentLikeRepository.remove(profileId, commentId);
        return effectRow == 1;
    }

    public CommentLikeEntity get(Integer profileId, String commentId) {
        Optional<CommentLikeEntity> optional = commentLikeRepository.getByProfileIdAndCommentId(profileId, commentId);
        return optional.orElse(null);
    }

}
