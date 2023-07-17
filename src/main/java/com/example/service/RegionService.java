package com.example.service;

import com.example.dto.RegionDTO;
import com.example.entity.RegionEntity;
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
        regionRepository.deleteById(id);
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> entities = regionRepository.findAll(Sort.by("order_number").descending());
        List<RegionDTO> dtoList = new LinkedList<>();
        entities.forEach(r -> {
            dtoList.add(toDTO(r));
        });
        if (dtoList.isEmpty()) throw new ItemNotFoundException("Region not found.");
        return dtoList;
    }

    public List<RegionDTO> getByLan(String lan) {

        return null;
    }

    public RegionDTO getById(Integer id) {
        Optional<RegionEntity> entity = regionRepository.findById(id);
        if (entity.isEmpty()) throw new ItemNotFoundException("Region not found.");
        return toDTO(entity.get());
    }


    private RegionDTO toDTO(RegionEntity entity) {
        return null;

    }
}
