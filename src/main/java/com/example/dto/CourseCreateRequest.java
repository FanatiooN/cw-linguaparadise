package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateRequest {
    private String name;
    private String coursPrice;
    private String description;
    private String avatar; // Base64 строка изображения
} 