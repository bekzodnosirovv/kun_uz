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

    public RegionDTO create(Integer prtId, RegionDTO dto) {
        isValidRegion(dto); // check
        RegionEntity entity = new RegionEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setPrtId(prtId);
        regionRepository.save(entity); // save
        dto.setId(entity.getId());
        return dto;
    }

    public void update(Integer regionId, RegionDTO dto) {
        isValidRegion(dto);  // TODO check ?
        RegionEntity entity = getById(regionId);
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        regionRepository.save(entity); // update
    }

    public void delete(Integer regionId) {
        getById(regionId); // check
        if (regionRepository.deletedById(regionId) != 1) {
            throw new ItemNotFoundException("Region not deleted");
        }  // update visible
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> entities = regionRepository.findAll(Sort.by("orderNumber").descending());
        List<RegionDTO> dtoList = new LinkedList<>();
        entities.forEach(entity -> {
            dtoList.add(toDTO(entity));
        });
        return dtoList; // response
    }

    public List<RegionDTO> getByLan(Language lan) {
        List<RegionEntity> entities = regionRepository.findAllByVisibleTrueOrderByOrderNumber();
        List<RegionDTO> dtoList = new LinkedList<>();
        entities.forEach(entity -> {
            RegionDTO dto = new RegionDTO();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());
            switch (lan) {
                case en -> dto.setName(entity.getNameEn());
                case ru -> dto.setName(entity.getNameRu());
                default -> dto.setName(entity.getNameUz());
            }
            dtoList.add(dto);
        });
        return dtoList;
    }

    public RegionEntity getById(Integer regionId) {
        return regionRepository.findById(regionId).
                orElseThrow(() -> new ItemNotFoundException("Region not found."));
    }

    private RegionDTO toDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private void isValidRegion(RegionDTO dto) {
        if (dto.getOrderNumber() == null) throw new AppBadRequestException("Order number required");
        if (dto.getNameUz() == null) throw new AppBadRequestException("Name uz required");
        if (dto.getNameEn() == null) throw new AppBadRequestException("Name en required");
        if (dto.getNameRu() == null) throw new AppBadRequestException("Name ru required");
    }
}
