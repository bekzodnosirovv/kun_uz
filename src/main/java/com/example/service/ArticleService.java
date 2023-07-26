package com.example.service;

import com.example.dto.*;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.mapper.ArticleShortMapper;
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
    private RegionService regionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleTypeRepository articleTypeRepository;
    @Autowired
    private ArticleTypesService articleTypesService;

    public ArticleDTO create(Integer moderatorId, ArticleDTO dto) {
        isValidArticle(dto); // check
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setImage_id(dto.getImageId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setRegionId(dto.getRegionId());
        entity.setModeratorId(moderatorId);
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleRepository.save(entity); // save
        articleTypesService.create(entity.getId(), dto.getArticleTypes()); // save types
        // response dto
        dto.setId(entity.getId());
        return dto;
    }

    public void update(String articleId, ArticleDTO dto, Integer moderatorId) {
        isValidArticle(dto);  // TODO checking ?
        ArticleEntity entity = getById(articleId);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setImage_id(dto.getImageId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setRegionId(dto.getRegionId());
        entity.setModeratorId(moderatorId);
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleRepository.save(entity); // update
        articleTypesService.merge(entity.getId(), dto.getArticleTypes()); // save types
    }

    public void delete(String articleId) {
        getById(articleId); // check
        articleRepository.deletedById(articleId); // update visible
    }


    public void changeStatus(String articleId, Integer publisherId, ArticleStatus status) {
        getById(articleId); // check article
        articleRepository.changeStatus(articleId, status, LocalDateTime.now(), publisherId); // update status
    }

    // get article 5 by type
    public List<ArticleDTO> getLastFiveByType(Integer typeId) {
        List<ArticleEntity> entityList = articleRepository.getListByType(typeId, 5, ArticleStatus.PUBLISHED);
        if (entityList.isEmpty()) return new LinkedList<>();
        // response article short info
        return entityList.stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList();
    }

    // get article 3 by type
    public List<ArticleDTO> getLastThreeByType(Integer typeId) {
        List<ArticleEntity> entityList = articleRepository.getListByType(typeId, 3, ArticleStatus.PUBLISHED);
        if (entityList.isEmpty()) return new LinkedList<>();
        // response article short info
        return entityList.stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList();
    }

    // get article 8
    public List<ArticleDTO> getLastEight(List<String> list) {
        List<ArticleShortMapper> shortMappers = articleRepository.getEightList(ArticleStatus.PUBLISHED, list);
        if (shortMappers.isEmpty()) return new LinkedList<>();
        // response article short info
        return shortMappers.stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImageId(), e.getPublishedDate())).toList();

    }

    public ArticleDTO getByIdAndLan(String articleId, Language lan) {
        Optional<ArticleEntity> optional = articleRepository.getByIdAndStatusAndVisibleIsTrue(articleId, ArticleStatus.PUBLISHED);
        if (optional.isEmpty()) return new ArticleDTO();
        // response article full info
        return getFullInfo(optional.get(), lan);
    }

    //  Get Last 4 Article By Types and except given article id
    public List<ArticleDTO> getLastFourByType(String articleId, Integer typeId) {
        List<ArticleEntity> entityList = articleRepository.getLastFourByType(typeId, ArticleStatus.PUBLISHED, articleId, 4);
        if (entityList.isEmpty()) return new LinkedList<>();
        return entityList.stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList();
    }

    public List<ArticleShortInfoDTO> getLastFiveByCategory(Integer catId) {
//        List<ArticleEntity> entityList=articleRepository.
        return null;
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
            dto.setImageId(entity1.getImage().getId());
            dto.setPublishedDate(entity1.getPublishedDate());
            dto.setImageURL(entity1.getImage().getPath());
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

    // get article full info
    public ArticleDTO getFullInfo(ArticleEntity entity, Language lan) {
        ArticleDTO fullInfo = new ArticleDTO();
        fullInfo.setId(entity.getId());
        fullInfo.setTitle(entity.getTitle());
        fullInfo.setDescription(entity.getDescription());
        fullInfo.setContent(entity.getContent());
        fullInfo.setSharedCount(entity.getSharedCount());
        fullInfo.setPublishedDate(entity.getPublishedDate());
        fullInfo.setViewCount(entity.getViewCount());
        // get region
        RegionEntity region = regionService.getById(entity.getRegionId());
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(region.getId());
        switch (lan) {
            case en -> regionDTO.setName(region.getNameEn());
            case ru -> regionDTO.setName(region.getNameRu());
            default -> regionDTO.setName(region.getNameUz());
        }
        fullInfo.setRegionDTO(regionDTO); // set region
        // get category
        CategoryEntity category = categoryService.getById(entity.getCategoryId());
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        switch (lan) {
            case en -> categoryDTO.setName(category.getNameEn());
            case ru -> categoryDTO.setName(category.getNameRu());
            default -> categoryDTO.setName(category.getNameUz());
        }
        fullInfo.setCategoryDTO(categoryDTO); // set category

        // TODO fullInfo set like count and tag list


        return fullInfo;
    }

    // get article short info
    public ArticleDTO getShortInfo(String articleId, String title, String description, String imageId, LocalDateTime publishDate) {
        ArticleDTO shortInfo = new ArticleDTO();
        shortInfo.setId(articleId);
        shortInfo.setTitle(title);
        shortInfo.setDescription(description);
        shortInfo.setPublishedDate(publishDate);
        // get attach
        AttachEntity attachEntity = attachService.get(imageId);
        // create path
        String path = attachEntity.getPath() + attachEntity.getId() + attachEntity.getExtension();
        shortInfo.setAttachDTO(new AttachDTO(attachEntity.getId(), path));
        return shortInfo;
    }

    public ArticleEntity getById(String articleId) {
        return articleRepository.findById(articleId).
                orElseThrow(() -> new ItemNotFoundException("Article not found"));
    }

    public ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setStatus(entity.getStatus());
        dto.setModeratorId(entity.getModerator().getId());
        dto.setPublisherId(entity.getPublisher().getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
        return dto;
    }

    private void isValidArticle(ArticleDTO dto) {
        if (dto.getTitle() == null) throw new AppBadRequestException("Title required");
        if (dto.getDescription() == null) throw new AppBadRequestException("Description required");
        if (dto.getContent() == null) throw new AppBadRequestException("Content required");
        if (dto.getImageId() == null) throw new AppBadRequestException("Image required");
        if (dto.getRegionId() == null) throw new AppBadRequestException("Region required");
        if (dto.getCategoryId() == null) throw new AppBadRequestException("Category required");
        if (dto.getArticleTypes() == null) throw new AppBadRequestException("Article type required");
    }

}
