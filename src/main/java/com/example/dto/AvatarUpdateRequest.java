package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvatarUpdateRequest {
    private String avatar; // Base64 строка изображения
} 