package com.example.service;

import com.example.dto.ArticleTypeDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.enums.Language;
import com.example.exp.ItemNotFoundException;
import com.example.repository.ArticleTypeRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    private ArticleTypeRepository articleTypeRepository;

    public ArticleTypeDTO create(ArticleTypeDTO dto) {
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        articleTypeRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public void update(Integer id, ArticleTypeDTO dto) {
        getById(id);
        ArticleTypeEntity entity = articleTypeRepository.findById(id).get();
        if (dto.getOrderNumber() != null) entity.setOrderNumber(dto.getOrderNumber());
        if (dto.getNameUz() != null) entity.setNameUz(dto.getNameUz());
        if (dto.getNameEn() != null) entity.setNameEn(dto.getNameEn());
        if (dto.getNameRu() != null) entity.setNameRu(dto.getNameRu());
        articleTypeRepository.save(entity);
    }

    public void delete(Integer id) {
        getById(id);
        articleTypeRepository.deletedById(id);
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
                case uz -> dto.setName(entity.getNameUz());
                case en -> dto.setName(entity.getNameEn());
                case ru -> dto.setName(entity.getNameRu());
            }
            dtoList.add(dto);
        });
        if (dtoList.isEmpty()) throw new ItemNotFoundException("Article type not found.");
        return dtoList;
    }

    public ArticleTypeDTO getById(Integer id) {
        Optional<ArticleTypeEntity> entity = articleTypeRepository.findById(id);
        if (entity.isEmpty()) throw new ItemNotFoundException("Article type not found.");
        return toDTO(entity.get());
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
}
