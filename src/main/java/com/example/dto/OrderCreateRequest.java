package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {
    private List<CourseInCart> courses;
    private BigDecimal totalCost;
    
    @Getter
    @Setter
    public static class CourseInCart {
        private int id;
        private String name;
        private int price;
        private String description;
        private String avatar;
    }
} 