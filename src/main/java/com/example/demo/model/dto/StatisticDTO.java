package com.example.demo.model.dto;

import lombok.Data;

@Data
public class StatisticDTO {

    private String month;
    private int totalOrderCount;
    private int totalPurchasedBook;
    private int totalPurchasedAmount;

}
