package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.ArticleRepository;
import com.example.repository.AttachRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AttachRepository attachRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public ArticleDTO create(ArticleDTO dto) {
        isValidArticle(dto);

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());

        if (dto.getDescription() == null) {
            entity.setDescription(dto.getContent().substring(dto.getContent().indexOf(".") + 1));
        } else entity.setDescription(dto.getDescription());

        entity.setContent(dto.getContent());

        if (dto.getSharedCount() == null) entity.setSharedCount(0);
        else entity.setSharedCount(dto.getSharedCount());

        if (dto.getImageId() != null) {
            Optional<AttachEntity> optionalAttach = attachRepository.findById(dto.getImageId());
            optionalAttach.ifPresent(entity::setImageId);
        }
        if (dto.getRegionId() != null) {
            Optional<RegionEntity> optionalRegion = regionRepository.findById(dto.getRegionId());
            optionalRegion.ifPresent(entity::setRegionId);
        }
        if (dto.getCategoryId() != null) {
            Optional<CategoryEntity> optionalCategory = categoryRepository.findById(dto.getCategoryId());
            optionalCategory.ifPresent(entity::setCategoryId);
        }

        ProfileEntity profileEntity = new ProfileEntity();
//        profileEntity.setId(id);
//        entity.setModeratorId(profileEntity);
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);

        articleRepository.save(entity);
        dto.setId(entity.getId());

        return dto;
    }

    public void update() {

    }

    public void delete(String articleId) {
        getById(articleId);
        articleRepository.deletedById(articleId);
    }

    public void changeStatus(String articleId, Integer profileId, ArticleStatus status) {
        getById(articleId);
        ProfileEntity entity = new ProfileEntity();
        entity.setId(profileId);
        articleRepository.changeStatus(articleId, status, LocalDateTime.now(), entity);

    }

    public void getLastFiveByType(Integer id) {

    }

    public void getById(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("Article not found");
    }

    private void isValidArticle(ArticleDTO dto) {
        if (dto.getTitle() == null) throw new AppBadRequestException("Title required");
        if (dto.getContent() == null) throw new AppBadRequestException("Content required");

    }
}
