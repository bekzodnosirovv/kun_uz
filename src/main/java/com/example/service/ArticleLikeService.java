package com.example.service;

import com.example.entity.ArticleLikeEntity;
import com.example.enums.LikeStatus;
import com.example.repository.ArticleLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleLikeService {

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    public boolean like(Integer profileId, String articleId) {
        if (articleId==null) return false; // check
        ArticleLikeEntity entity = get(profileId, articleId);
        if (entity != null) remove(profileId, articleId);

        entity = new ArticleLikeEntity();
        entity.setProfileId(profileId);
        entity.setArticleId(articleId);
        entity.setStatus(LikeStatus.LIKE);
        articleLikeRepository.save(entity);

        return true;
    }

    public boolean dislike(Integer profileId, String articleId) {
        if (articleId==null) return false; // check
        ArticleLikeEntity entity = get(profileId, articleId);
        if (entity != null) remove(profileId, articleId);

        entity = new ArticleLikeEntity();
        entity.setProfileId(profileId);
        entity.setArticleId(articleId);
        entity.setStatus(LikeStatus.DISLIKE);
        articleLikeRepository.save(entity);

        return true;

    }

    public boolean remove(Integer profileId, String articleId) {
        if (profileId == null || articleId == null) return false;
        int effectRow = articleLikeRepository.remove(profileId, articleId);
        return effectRow == 1;

    }

    public ArticleLikeEntity get(Integer profileId, String articleId) {
        if (articleId == null) return null;
        Optional<ArticleLikeEntity> optional = articleLikeRepository.getByProfileIdAndArticleId(profileId, articleId);
        return optional.orElse(null);
    }
}
