package com.example.demo.model.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="statistic_metrics")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String month;
    private long totalOrderCount;
    private long totalPurchasedBook;
    private double totalPurchasedAmount;
}
