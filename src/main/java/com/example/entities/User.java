package com.example.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "user_name")
    private String username;
    private String name;
    private String surName;
    private String password;
    private LocalDate birthDate;
    @Column(precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
    @Column(columnDefinition = "TEXT")
    private String avatar;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.USER;
    @OneToMany
    private List<Order> orders;
}
