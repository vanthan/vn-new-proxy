package com.vanthan.vn.service;

import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.OrderForm;
import com.vanthan.vn.dto.OrderResult;
import com.vanthan.vn.model.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    BaseResponse<String> createOrder(OrderForm form, String token);
    BaseResponse<OrderResult> getOrder(int orderId);
    BaseResponse<List<Order>> findOrdersByUserId(int userId);
}
