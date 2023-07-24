package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.dto.ArticleShortInfoDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
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
    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    public ArticleDTO create(Integer id, ArticleDTO dto) {
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
        profileEntity.setId(id);
        entity.setModeratorId(profileEntity);
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);

        articleRepository.save(entity);
        dto.setId(entity.getId());

        return dto;
    }

    public void update(String articleId, ArticleDTO dto, Integer profileId) {

    }

    public void delete(String articleId) {
        getById(articleId);
        articleRepository.deletedById(articleId);
    }

    public ArticleDTO changeStatus(String articleId, Integer profileId, ArticleStatus status) {
        getById(articleId);
        ProfileEntity entity = new ProfileEntity();
        entity.setId(profileId);
        articleRepository.changeStatus(articleId, status, LocalDateTime.now(), entity);
        Optional<ArticleEntity> articleEntity = articleRepository.findById(articleId);
        return toDTO(articleEntity.get());
    }

    public List<ArticleShortInfoDTO> getLastFiveByType(Integer id) {
        return articleRepository.getListByType(id, 5);
    }

    public List<ArticleShortInfoDTO> getLastThreeByType(Integer id) {
        return articleRepository.getListByType(id, 3);
    }

    public List<ArticleShortInfoDTO> getLastEight(List<String> list) {
        return articleRepository.getEightList(list);
    }

    public PageImpl<ArticleShortInfoDTO> byCategory(Integer catId, Integer page, Integer size) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(catId);
        Pageable pageable = PageRequest.of(page, size);
        List<ArticleShortInfoDTO> dtoList = new LinkedList<>();
        Page<ArticleEntity> entityPage = articleRepository.findAllByCategoryId(entity, pageable);
        entityPage.getContent().forEach(entity1 -> {
            ArticleShortInfoDTO dto = new ArticleShortInfoDTO();
            dto.setId(entity1.getId());
            dto.setTitle(entity1.getTitle());
            dto.setDescription(entity1.getDescription());
            dto.setImageId(entity1.getImageId().getId());
            dto.setPublishedDate(entity1.getPublishedDate());
            dto.setImageURL(entity1.getImageId().getPath());
            dtoList.add(dto);
        });

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    public Integer increaseViewCountById(String id) {
        getById(id);
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        return optional.get().getViewCount();
    }

    public Integer increaseShareCountById(String id) {
        getById(id);
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        return optional.get().getSharedCount();
    }

    public void getById(String id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()) throw new ItemNotFoundException("Article not found");
    }

    public ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setStatus(entity.getStatus());
        dto.setModeratorId(entity.getModeratorId().getId());
        dto.setPublisherId(entity.getPublisherId().getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
        return dto;
    }

    private void isValidArticle(ArticleDTO dto) {
        if (dto.getTitle() == null) throw new AppBadRequestException("Title required");
        if (dto.getContent() == null) throw new AppBadRequestException("Content required");
    }

}
