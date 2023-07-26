package com.example.repository;


import com.example.dto.ArticleShortInfoDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ArticleStatus;
import com.example.mapper.ArticleShortMapper;
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
import java.util.Optional;

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
                     @Param("publisher") Integer publisherId);


    @Query(value = "from ArticleEntity  as a inner join a.articleTypes as at " +
            "where at.articleTypeId=:typeId and a.status=:status and a.visible=true " +
            "order by a.publishedDate desc limit :lim")
    List<ArticleEntity> getListByType(@Param("typeId") Integer articleTypeId,
                                      @Param("lim") Integer limit,
                                      @Param("status") ArticleStatus status);

    @Query(value = "select a.id,a.title,a.description, a.image_id as imageId, a.published_date as publishedDate " +
            "from article  where  status=:status " +
            "and visible=true and id not in (:list) " +
            "order by created_date desc limit 8", nativeQuery = true)
    List<ArticleShortMapper> getEightList(@Param("status") ArticleStatus status,
                                          @Param("list") List<String> list);

    Optional<ArticleEntity> getByIdAndStatusAndVisibleIsTrue(String id, ArticleStatus status);

    @Query("from ArticleEntity as a inner join a.articleTypes as at " +
            "where at.articleTypeId=:typeId and a.status=:status and a.visible=true " +
            "and a.id=:articleId order by a.createdDate desc limit :lim")
    List<ArticleEntity> getLastFourByType(@Param("typeId") Integer typeId,
                                          @Param("status") ArticleStatus status,
                                          @Param("articleId") String articleId,
                                          @Param("lim") Integer limit);
    Page<ArticleEntity> findAllByCategoryId(CategoryEntity entity, Pageable pageable);

}
