package com.example.demo.shop.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductsDTO{
    private String productName;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String image;
}
