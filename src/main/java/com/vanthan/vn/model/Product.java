package com.vanthan.vn.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Table(name = "vn_product")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String sku;
    private String name;
    private int quantity;
    private int price;
    private String createdBy;

    public Product() {

    }
}
