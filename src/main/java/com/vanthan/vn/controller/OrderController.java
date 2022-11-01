package com.vanthan.vn.controller;


import com.vanthan.vn.dto.BaseResponse;
import com.vanthan.vn.dto.OrderForm;
import com.vanthan.vn.dto.OrderResult;
import com.vanthan.vn.jwt.AuthTokenFilter;
import com.vanthan.vn.jwt.JwtUtils;
import com.vanthan.vn.model.Order;
import com.vanthan.vn.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    private final AuthTokenFilter authTokenFilter;

    @Autowired
    public OrderController(OrderService orderService, JwtUtils jwtUtils, AuthTokenFilter authTokenFilter) {
        this.orderService = orderService;
        this.authTokenFilter = authTokenFilter;
    }

    @PostMapping(value = "/orders")
    public ResponseEntity<BaseResponse<OrderResult>> createOrder(@RequestBody OrderForm form, HttpServletRequest request) {
        try {
            // get username from http request
            String token = authTokenFilter.parseJwt(request);
            return ResponseEntity.ok(orderService.createOrder(form,token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<BaseResponse<OrderResult>> getOrder(@PathVariable int id) {
        return ResponseEntity.ok(orderService.getOrder(id));

    }

    @GetMapping(value = "getOrders/{userId}")
    public ResponseEntity<BaseResponse<List<Order>>> findOrdersByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(orderService.findOrdersByUserId(userId));
    }

//    @PostMapping(value = "orders/{id}")
//    public ResponseEntity<BaseResponse<OrderResult>> updateOrder(@PathVariable int id){
//        return ResponseEntity.ok(orderService.updateByOrderId(id));
//    }


}

