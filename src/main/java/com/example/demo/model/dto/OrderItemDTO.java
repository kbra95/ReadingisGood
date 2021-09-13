package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
@Data
public class OrderItemDTO {
    @JsonIgnore
    private Long id;
    private Long bookId;

    @Min(1)
    @Max(10)
    private int quantity;
}
