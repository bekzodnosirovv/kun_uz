package com.example.repository;

import com.example.dto.CommentFilterDTO;
import com.example.dto.FilterResultDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.entity.CommentEntity;
import com.example.entity.ProfileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomRepository {
    @Autowired
    private EntityManager entityManager;

    public List<ProfileEntity> filterProfile(ProfileFilterDTO filterDTO) {

        StringBuilder selectQueryBuilder = new StringBuilder("select s from ProfileEntity as s where 1=1");
        StringBuilder whereQuery = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (filterDTO.getName() != null) {
            whereQuery.append(" and s.name =:name");
            params.put("name", filterDTO.getName());
        }
        if (filterDTO.getSurname() != null) {
            whereQuery.append(" and s.surname =:surname");
            params.put("surname", filterDTO.getSurname());
        }
        if (filterDTO.getPhone() != null) {
            whereQuery.append(" and s.phone =:phone");
            params.put("phone", filterDTO.getPhone());
        }
        if (filterDTO.getRole() != null) {
            whereQuery.append(" and s.role =:role");
            params.put("role", filterDTO.getRole().toString());
        }

        if (filterDTO.getCreatedDateFrom() != null) {
            whereQuery.append(" and s.createdDate >=:fromDate");
            params.put("fromDate", LocalDateTime.of(filterDTO.getCreatedDateFrom(), LocalTime.MIN));
        }
        if (filterDTO.getCreatedDateTo() != null) {
            whereQuery.append(" and s.createdDate <=:ToDate");
            params.put("ToDate", LocalDateTime.of(filterDTO.getCreatedDateTo(), LocalTime.MAX));
        }
        Query selectQuery = entityManager.createQuery(selectQueryBuilder.append(whereQuery).toString());
        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
        }

        return selectQuery.getResultList();
    }

    public FilterResultDTO<CommentEntity> filterComment(CommentFilterDTO filterDTO,Integer page,Integer size) {
        StringBuilder selectQueryBuilder = new StringBuilder("select s from CommentEntity as s where 1=1");
        StringBuilder countQueryBuilder = new StringBuilder("select count(s) from CommentEntity as s where 1=1");
        StringBuilder whereQuery = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (filterDTO.getId() != null) {
            whereQuery.append(" and s.id =:id");
            params.put("id", filterDTO.getId());
        }
        if (filterDTO.getProfileId() != null) {
            whereQuery.append(" and s.profileId =:profileId");
            params.put("profileId", filterDTO.getProfileId());
        }
        if (filterDTO.getArticleId() != null) {
            whereQuery.append(" and s.articleId =:articleId");
            params.put("articleId", filterDTO.getArticleId());
        }
        if (filterDTO.getCreatedDateFrom() != null) {
            whereQuery.append(" and s.createdDate >=:createdDateFrom");
            params.put("createdDateFrom", LocalDateTime.of(filterDTO.getCreatedDateFrom(), LocalTime.MIN));
        }
        if (filterDTO.getCreatedDateTo() != null) {
            whereQuery.append(" and s.createdDate <=:createdDateTo");
            params.put("createdDateTo", LocalDateTime.of(filterDTO.getCreatedDateTo(), LocalTime.MAX));
        }

        Query selectQuery = entityManager.createQuery(selectQueryBuilder.append(whereQuery).toString());
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);

        Query countQuery = entityManager.createQuery(countQueryBuilder.append(whereQuery).toString());

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }

        List<CommentEntity> entityList = selectQuery.getResultList();
        Long totalCount = (Long) countQuery.getSingleResult();
        return new FilterResultDTO<>(entityList, totalCount);
    }


}
