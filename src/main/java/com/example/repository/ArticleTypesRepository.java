package com.example.repository;

import com.example.entity.ArticleTypesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleTypesRepository extends CrudRepository<ArticleTypesEntity, Integer> {

    @Query("select a.articleTypeId from ArticleTypesEntity as a where a.articleId=:articleId")
    List<Integer> getOldAticleTypeList(@Param("articleId") String articleId);

    void deleteByArticleIdAndAndArticleTypeId(String articleId,Integer typeId);

    void deleteByArticleId(String articleId);
}
