package com.example.repository;


import com.example.entity.ArticleEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ArticleStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
}
