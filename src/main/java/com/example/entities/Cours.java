package com.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class Cours
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String coursPrice;
    @Column(length = 500)
    private String description;
    @Column(columnDefinition = "TEXT")
    private String avatar;
    @OneToMany(mappedBy = "cours")
    private List<OrderItem> orderItem;
}
