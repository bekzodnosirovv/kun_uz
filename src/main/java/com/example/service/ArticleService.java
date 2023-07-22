package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.exp.AppBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    @Autowired
    private RegionService regionService;
    @Autowired
    private CategoryService categoryService;

    public ArticleDTO create(Integer id, ArticleDTO dto) {
        isValidArticle(dto);
        regionService.getById(dto.getRegionId().getId());
        categoryService.getById(dto.getCategoryId().getId());


        return null;
    }

    public void update() {

    }

    public void delete(){

    }
    public void changeStatus(){


    }

    private void isValidArticle(ArticleDTO dto) {
        if (dto.getTitle() == null) throw new AppBadRequestException("Title required");
        if (dto.getDescription() == null) throw new AppBadRequestException("Description required");
        if (dto.getContent() == null) throw new AppBadRequestException("Content required");
        if (dto.getSharedCount() == null) throw new AppBadRequestException("Shared count required");
        if (dto.getRegionId() == null) throw new AppBadRequestException("Region required");
        if (dto.getCategoryId() == null) throw new AppBadRequestException("Category required");

    }
}
