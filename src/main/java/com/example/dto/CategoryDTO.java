package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private Integer id;
    private Integer orderNumber;
    private String name;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private Boolean visible;
    private LocalDateTime createdDate;
}
