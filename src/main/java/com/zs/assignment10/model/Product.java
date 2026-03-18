package com.zs.assignment10.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Product.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Product {
    private Integer id;
    private String name;
    private Double price;
}