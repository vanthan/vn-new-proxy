package com.vanthan.vn.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vn_order")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   // private String deliveryCode;
    private int userId;
    private String username;
    private String email;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "orderId")
    private List<OrderItem> items;
    private int totalItems;
    private int totalCost;
    private String paymentMethod;
    private String status;
   // private String paymentId;



}

