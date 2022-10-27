package com.vanthan.vn.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    private List<OrderLineForm> orderLines;
}
