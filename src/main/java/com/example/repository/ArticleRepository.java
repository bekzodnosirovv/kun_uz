package com.example.repository;


import com.example.dto.ArticleShortInfoDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends CrudRepository<ArticleEntity, String>,
        PagingAndSortingRepository<ArticleEntity, String> {
    @Transactional
    @Modifying
    @Query("update ArticleEntity set visible=false where id=:id")
    int deletedById(@Param("id") String id);

    @Transactional
    @Modifying
    @Query("update ArticleEntity set status=:status,publishedDate=:publishedDate,publisherId=:publisher where id=:id")
    int changeStatus(@Param("id") String id,
                     @Param("status") ArticleStatus status,
                     @Param("publishedDate") LocalDateTime publishedDate,
                     @Param("publisher") ProfileEntity profileEntity);


    @Query(value = "select new com.example.dto.ArticleShortInfoDTO(ar.id,ar.title,ar.description,att.id,att.path) from article  ar " +
            "left join attach att on ar.image_id=att.id " +
            "where ar.id in (select article_id from article_types " +
            "where article_type_id=:typeId) and ar.status=PUBLISHED " +
            "order by ar.created_date desc limit :limit", nativeQuery = true)
    List<ArticleShortInfoDTO> getListByType(@Param("typeId") Integer articleTypeId,
                                            @Param("limit") Integer limit);

    @Query(value = "select new com.example.dto.ArticleShortInfoDTO(ar.id,ar.title,ar.description,att.id,att.path) from article  ar " +
            "left join attach att on ar.image_id=att.id " +
            "where ar.id not in (:list) and ar.status=PUBLISHED " +
            "order by created_date desc limit 8", nativeQuery = true)
    List<ArticleShortInfoDTO> getEightList(@Param("list") List<String> list);

    Page<ArticleEntity> findAllByCategoryId(CategoryEntity entity, Pageable pageable);

}
