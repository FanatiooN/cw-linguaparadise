package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BalanceUpdateRequest {
    private BigDecimal amount;
    private String paymentMethod;
} 