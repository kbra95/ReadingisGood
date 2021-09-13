package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PageResponse<T> {
    private List<T> content;
    private int totalPages;
    private int number;
    private int size;
    private long totalElements;

}