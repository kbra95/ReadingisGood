package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class BookDTO {
    @JsonIgnore
    private Long id;
    @NotEmpty(message = " can not be empty")
    private String name;
    @NotEmpty(message = " can not be empty")
    private String author;
    @NotNull(message = " can not be null")
    @Min(0)
    private Double price;
    @NotEmpty(message = " can not be empty")
    private String isbn;
    @Min(0)
    private int stock;
}
