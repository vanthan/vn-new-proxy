package com.vanthan.vn.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vn_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;
    private int orderId;
    private int productId;
    private String productName;
    private int quantity;
    private int listPrice; // gia niem yet
}
