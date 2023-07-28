package com.example.service;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.RegionDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.ArticleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    public ArticleTypeDTO create(Integer prtId, ArticleTypeDTO dto) {
        isValidArticleType(dto); // check
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setPrtId(prtId);
        articleTypeRepository.save(entity); // save
        // response
        dto.setId(entity.getId());
        return dto;
    }

    public void update(Integer typeId, ArticleTypeDTO dto) {
        isValidArticleType(dto); // TODO check ?
        ArticleTypeEntity entity = getById(typeId);
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        articleTypeRepository.save(entity); // update
    }

    public void delete(Integer typeId) {
        getById(typeId); // check
        if (articleTypeRepository.deletedById(typeId) != 1) {
            throw new ItemNotFoundException("Type not deleted");
        }   // update visible
    }

    public PageImpl<ArticleTypeDTO> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderNumber").descending());
        Page<ArticleTypeEntity> pageList = articleTypeRepository.findAll(pageable);
        return new PageImpl<>(pageList.getContent().stream().map(this::toDTO).toList(), pageable, pageList.getTotalElements());
    }

    public List<ArticleTypeDTO> getByLan(Language lan) {
        List<ArticleTypeEntity> entities = articleTypeRepository.findAllByVisibleTrueOrderByOrderNumber();
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        entities.forEach(entity -> {
            ArticleTypeDTO dto = new ArticleTypeDTO();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());
            switch (lan) {
                case en -> dto.setName(entity.getNameEn());
                case ru -> dto.setName(entity.getNameRu());
                default -> dto.setName(entity.getNameUz());
            }
            dtoList.add(dto);
        });
        // response
        return dtoList;
    }

    public ArticleTypeEntity getById(Integer typeId) {
        return articleTypeRepository.findById(typeId).
                orElseThrow(() -> new ItemNotFoundException("Article type not found."));
    }

    private ArticleTypeDTO toDTO(ArticleTypeEntity entity) {
        ArticleTypeDTO dto = new ArticleTypeDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private void isValidArticleType(ArticleTypeDTO dto) {
        if (dto.getOrderNumber() == null) throw new AppBadRequestException("Order number required");
        if (dto.getNameUz() == null) throw new AppBadRequestException("Name uz required");
        if (dto.getNameEn() == null) throw new AppBadRequestException("Name en required");
        if (dto.getNameRu() == null) throw new AppBadRequestException("Name ru required");
    }
}
