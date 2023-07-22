package com.example.service;

import com.example.dto.RegionDTO;
import com.example.entity.RegionEntity;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
import com.example.exp.ItemNotFoundException;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO create(RegionDTO dto) {
        isValidRegion(dto);
        RegionEntity entity = new RegionEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        regionRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public void update(Integer id, RegionDTO dto) {
        getById(id);
        RegionEntity entity = regionRepository.findById(id).get();
        if (dto.getOrderNumber() != null) entity.setOrderNumber(dto.getOrderNumber());
        if (dto.getNameUz() != null) entity.setNameUz(dto.getNameUz());
        if (dto.getNameEn() != null) entity.setNameEn(dto.getNameEn());
        if (dto.getNameRu() != null) entity.setNameRu(dto.getNameRu());
        regionRepository.save(entity);
    }

    public void delete(Integer id) {
        getById(id);
        regionRepository.deletedById(id);
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> entities = regionRepository.findAll(Sort.by("orderNumber").descending());
        List<RegionDTO> dtoList = new LinkedList<>();
        entities.forEach(r -> {
            dtoList.add(toDTO(r));
        });
        if (dtoList.isEmpty()) throw new ItemNotFoundException("Region not found.");
        return dtoList;
    }

    public List<RegionDTO> getByLan(Language lan) {
        List<RegionEntity> entities = regionRepository.findAllByVisibleTrueOrderByOrderNumber();
        List<RegionDTO> dtoList = new LinkedList<>();
        entities.forEach(entity -> {
            RegionDTO dto = new RegionDTO();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());
            switch (lan) {
                case uz -> dto.setName(entity.getNameUz());
                case en -> dto.setName(entity.getNameEn());
                case ru -> dto.setName(entity.getNameRu());

            }

            dtoList.add(dto);
        });
        if (dtoList.isEmpty()) throw new ItemNotFoundException("Region not found.");
        return dtoList;
    }

    public void getById(Integer id) {
        Optional<RegionEntity> entity = regionRepository.findById(id);
        if (entity.isEmpty()) throw new ItemNotFoundException("Region not found.");
        toDTO(entity.get());
    }


    private RegionDTO toDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setVisible(entity.isVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private void isValidRegion(RegionDTO dto){
        if (dto.getOrderNumber()==null) throw new AppBadRequestException("Order number required");

    }
}
