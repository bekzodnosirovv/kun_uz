package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.NoArgsConstructor;


@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDTO extends SuperDTO {
}
