package com.vanthan.vn.service.impl;


import com.vanthan.vn.dto.*;
import com.vanthan.vn.dto.OrderResult;
import com.vanthan.vn.jwt.AuthTokenFilter;
import com.vanthan.vn.jwt.JwtUtils;
import com.vanthan.vn.model.*;
import com.vanthan.vn.repository.OrderDetailRepository;
import com.vanthan.vn.repository.OrderRepository;
import com.vanthan.vn.repository.ProductRepository;
import com.vanthan.vn.repository.UserRepository;
import com.vanthan.vn.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final JwtUtils jwtUtils;

    private final AuthTokenFilter authTokenFilter;


    @Autowired
    public OrderServiceImp(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, JwtUtils jwtUtils, AuthTokenFilter authTokenFilter) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.jwtUtils = jwtUtils;
        this.authTokenFilter = authTokenFilter;
    }

    @Override
    public BaseResponse<String> createOrder(OrderForm form, String token) {
        BaseResponse<String> response = new BaseResponse<>();
        // get order item from the request list
        List<OrderLineForm> orderLines = form.getOrderLines();
        Order order = new Order();
        // get info from token: email + full name

        Map<String,Object> userInfo = jwtUtils.getClaimFromToken(token, claims -> {return claims;});
        int userid = Integer.parseInt(userInfo.get("id").toString());
        String email = userInfo.get("email").toString();
        String username = userInfo.get("username").toString();

        //get user info from token then set to order
        order.setUserId(userid);
        order.setUsername(username);
        order.setEmail(email);

        for (OrderLineForm orderLine : orderLines) {
            Optional<Product> maybeProduct = productRepository.findById(orderLine.getProductId());
            if (!maybeProduct.isPresent()) {
                response.setCode("001");
                response.setMessage("Product not found: " +  orderLine.getProductId());
                return response;
            }
            // get product
            Product product = maybeProduct.get();

            // update quantity in db
            if (product.getQuantity() < orderLine.getQuantity()){
                throw new IllegalArgumentException("Product is out of stock: " + orderLine.getProductId());
            }
            product.setQuantity(product.getQuantity() - orderLine.getQuantity());

            // save update product details
            productRepository.save(product);

            //set change of total quantity + total cost
            order.setTotalItems(order.getTotalItems() + orderLine.getQuantity());
            order.setTotalCost(order.getTotalCost() + (product.getPrice() * orderLine.getQuantity()));
            order.setPaymentMethod("CASH");
            order.setStatus("CREATED");

            orderRepository.save(order);
            //save order item
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setQuantity(orderLine.getQuantity());
            item.setListPrice(product.getPrice());
            orderDetailRepository.save(item);
        }
        response.setCode("00");
        response.setMessage("success");
        response.setBody("Created an order");
        return response;
    }

    @Override
    public BaseResponse<OrderResult> getOrder(int orderId) {
        BaseResponse<OrderResult> response = new BaseResponse<>();
        Optional<Order> maybeOrder = orderRepository.findById(orderId);

        // if existed
        if (!maybeOrder.isPresent()) {
            response.setCode("11");
            response.setMessage("Order not found: " + orderId);
            return response;
        }

        Order order = maybeOrder.get();
        // user result
        int userId = order.getUserId();
        String username = order.getUsername();
        String email = order.getEmail();
        UserResult userResult = new UserResult(userId, username, email);

        // order item list
        List<OrderItemResult> orderItemResultList = new ArrayList<>();
        for (OrderItem orderItem : order.getItems()){
            OrderItemResult orderItemResult = new OrderItemResult();
            orderItemResult.setProductId(orderItem.getProductId());
            orderItemResult.setProductName(orderItem.getProductName());
            orderItemResult.setQuantity(orderItem.getQuantity());
            orderItemResult.setPrice(orderItem.getListPrice());
            orderItemResult.setTotal(orderItem.getQuantity() * orderItem.getListPrice());
            orderItemResultList.add(orderItemResult);
        }

        OrderResult orderResult = new OrderResult();
        orderResult.setUserResult(userResult);
        orderResult.setItems(orderItemResultList);
        orderResult.setOrderId(orderId);
        orderResult.setTotalItems(order.getTotalItems());
        orderResult.setTotalCost(order.getTotalCost());
        orderResult.setPaymentMethod("CASH");
        orderResult.setStatus("CREATED");
        response.setBody(orderResult);
        return response;
    }

    @Override
    public BaseResponse<List<Order>> findOrdersByUserId(int userId) {
        BaseResponse<List<Order>> response = new BaseResponse<>();
        Optional<User> maybeUser = userRepository.findById(userId);

        // if existed
        if (!maybeUser.isPresent()) {
            response.setCode("11");
            response.setMessage("User not found: " + userId);
            return response;
        }

        response.setBody(orderRepository.findOrderByUserId(userId));
        return response;
    }
}