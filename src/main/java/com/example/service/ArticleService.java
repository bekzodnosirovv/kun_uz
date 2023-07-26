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
    private ArticleTypesService articleTypesService;
    @Autowired
    private CustomRepository customRepository;

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
        Optional<ArticleEntity> optional = articleRepository.getByIdAndStatusAndVisibleTrue(articleId, ArticleStatus.PUBLISHED);
        if (optional.isEmpty()) return new ArticleDTO();
        // response article full info
        return getFullInfo(optional.get(), lan);
    }

    //  Get Last 4 Article By Types and except given article id
    public List<ArticleDTO> getLastFourByType(String articleId, Integer typeId) {
        List<ArticleEntity> entityList = articleRepository.getLastFourByType(typeId, ArticleStatus.PUBLISHED, articleId);
        if (entityList.isEmpty()) return new LinkedList<>();
        return entityList.stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList();
    }

    // get 4 most read article
    public List<ArticleDTO> getMostReadList() {
        List<ArticleEntity> entityList = articleRepository.getListMostRead(ArticleStatus.PUBLISHED);
        if (entityList.isEmpty()) return new LinkedList<>();
        return entityList.stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList();
    }

    // get 4 by tag name
    public List<ArticleDTO> getByTagName(Integer tagId) {
        List<ArticleEntity> entityList = articleRepository.getListByTag(tagId, ArticleStatus.PUBLISHED);
        if (entityList.isEmpty()) return new LinkedList<>();
        return entityList.stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList();
    }

    // by type and region
    public List<ArticleDTO> getByTypeAndRegion(Integer typeId, Integer regionId) {
        List<ArticleEntity> entityList = articleRepository.getByTypeAndRegion(typeId, regionId, ArticleStatus.PUBLISHED);
        if (entityList.isEmpty()) return new LinkedList<>();
        return entityList.stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList();
    }

    // get region id pagination
    public PageImpl<ArticleDTO> getByRegionPagination(Integer regionId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleEntity> entityPage = articleRepository.findAllByRegionIdAndStatusAndVisibleTrue(regionId, ArticleStatus.PUBLISHED, pageable);
        return new PageImpl<>(entityPage.getContent().stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList(),
                pageable, entityPage.getTotalElements());
    }

    // get last 5 by category id
    public List<ArticleDTO> getLastFiveByCategory(Integer categoryId) {
        List<ArticleEntity> entityList = articleRepository.getLastFiveByCategory(ArticleStatus.PUBLISHED);
        if (entityList.isEmpty()) return new LinkedList<>();
        return entityList.stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList();
    }

    // get article by category pagination
    public PageImpl<ArticleDTO> getLastFiveByCategory(Integer categoryId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleEntity> entityPage = articleRepository.findAllByRegionIdAndStatusAndVisibleTrue(categoryId, ArticleStatus.PUBLISHED, pageable);
        return new PageImpl<>(entityPage.getContent().stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList(),
                pageable, entityPage.getTotalElements());
    }

    public Integer increaseViewCountById(String articleId) {
        ArticleEntity entity = getById(articleId);
        return entity.getViewCount();
    }

    public Integer increaseShareCountById(String categoryId) {
        ArticleEntity entity = getById(categoryId);
        return entity.getSharedCount();
    }

    // filter
    public PageImpl<ArticleDTO> filter(ArticleFilterDTO filterDTO, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        FilterResultDTO<ArticleEntity> resultDTO = customRepository.filterArticle(filterDTO, page, size);
        return new PageImpl<>(resultDTO.getList().stream().map(e -> getShortInfo(e.getId(), e.getTitle(), e.getDescription(), e.getImage_id(), e.getPublishedDate())).toList(),
                pageable, resultDTO.getTotalCount());
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
        return articleRepository.findByIdAndStatusAndVisibleTrue(articleId, ArticleStatus.PUBLISHED).
                orElseThrow(() -> new ItemNotFoundException("Article not found"));
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
