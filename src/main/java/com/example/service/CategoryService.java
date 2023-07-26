package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.dto.RegionDTO;
import com.example.entity.CategoryEntity;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
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

    public CategoryDTO create(Integer prtId, CategoryDTO dto) {
        isValidCategory(dto); // check
        CategoryEntity entity = new CategoryEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setPrtId(prtId);
        categoryRepository.save(entity); // save
        // response
        dto.setId(entity.getId());
        return dto;
    }

    public void update(Integer id, CategoryDTO dto) {
        isValidCategory(dto); // TODO check ?
        CategoryEntity entity = getById(id);
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        categoryRepository.save(entity); // update
    }

    public void delete(Integer id) {
        getById(id); // check
        categoryRepository.deletedById(id); // update visible
    }

    public List<CategoryDTO> getAll() {
        Iterable<CategoryEntity> entities = categoryRepository.findAll(Sort.by("orderNumber").descending());
        List<CategoryDTO> dtoList = new LinkedList<>();
        entities.forEach(r -> {
            dtoList.add(toDTO(r));
        });
        return dtoList; // response list
    }

    public List<CategoryDTO> getByLan(Language lan) {
        List<CategoryEntity> entities = categoryRepository.findAllByVisibleTrueOrderByOrderNumber();
        List<CategoryDTO> dtoList = new LinkedList<>();
        entities.forEach(entity -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());
            switch (lan) {
                case en -> dto.setName(entity.getNameEn());
                case ru -> dto.setName(entity.getNameRu());
                default -> dto.setName(entity.getNameUz());
            }
            dtoList.add(dto);
        });
        return dtoList; // response list
    }

    public CategoryEntity getById(Integer id) {
        return categoryRepository.findById(id).
                orElseThrow(() -> new ItemNotFoundException("Category not found."));
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private void isValidCategory(CategoryDTO dto) {
        if (dto.getOrderNumber() == null) throw new AppBadRequestException("Order number required");
        if (dto.getNameUz() == null) throw new AppBadRequestException("Name uz required");
        if (dto.getNameEn() == null) throw new AppBadRequestException("Name en required");
        if (dto.getNameRu() == null) throw new AppBadRequestException("Name ru required");
    }
}
