package com.vanthan.vn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int orderId;
    private Timestamp createdAt;
    private UserResult userResult;
    private List<OrderItemResult> items;
    private int totalItems;
    private int totalCost;
    private String paymentMethod;
    private String status;
}
