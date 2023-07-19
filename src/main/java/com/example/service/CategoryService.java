package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.entity.CategoryEntity;
import com.example.enums.Language;
import com.example.exp.ItemNotFoundException;
import com.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        return dto;

    }

    public void update(Integer id, CategoryDTO dto) {
        getById(id);
        CategoryEntity entity = categoryRepository.findById(id).get();
        if (dto.getOrderNumber() != null) entity.setOrderNumber(dto.getOrderNumber());
        if (dto.getNameUz() != null) entity.setNameUz(dto.getNameUz());
        if (dto.getNameEn() != null) entity.setNameEn(dto.getNameEn());
        if (dto.getNameRu() != null) entity.setNameRu(dto.getNameRu());
        categoryRepository.save(entity);
    }

    public void delete(Integer id) {
        getById(id);
        categoryRepository.deletedById(id);
    }

    public List<CategoryDTO> getAll() {
        Iterable<CategoryEntity> entities = categoryRepository.findAll(Sort.by("orderNumber").descending());
        List<CategoryDTO> dtoList = new LinkedList<>();
        entities.forEach(r -> {
            dtoList.add(toDTO(r));
        });
        if (dtoList.isEmpty()) throw new ItemNotFoundException("Category not found.");
        return dtoList;

    }

    public List<CategoryDTO> getByLan(Language lan) {
        List<CategoryEntity> entities = categoryRepository.findAllByVisibleTrueOrderByOrderNumber();
        List<CategoryDTO> dtoList = new LinkedList<>();
        entities.forEach(entity -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());
            switch (lan) {
                case uz -> dto.setName(entity.getNameUz());
                case en -> dto.setName(entity.getNameEn());
                case ru -> dto.setName(entity.getNameRu());
            }
            dtoList.add(dto);
        });
        if (dtoList.isEmpty()) throw new ItemNotFoundException("Category not found.");
        return dtoList;

    }

    public void getById(Integer id) {
        Optional<CategoryEntity> entity = categoryRepository.findById(id);
        if (entity.isEmpty()) throw new ItemNotFoundException("Category not found.");
        toDTO(entity.get());
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setVisible(entity.isVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;

    }
}
