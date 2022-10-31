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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.vanthan.vn.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Log4j2
public class OrderServiceImp implements OrderService {

    @Value("${base.url.authen}")
    private String baseUrl;
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
    public BaseResponse<String> createOrder(OrderForm form, HttpServletRequest request) {
        BaseResponse<String> response = new BaseResponse<>();

        EmailDTO emailDTO = new EmailDTO();
        // Map props => set props email
        Map<String, Object> props = new HashMap<String, Object>();

        // get order item from the request list
        List<OrderLineForm> orderLines = form.getOrderLines();
        Order order = new Order();
        // get info from token: email + full name
        String token = authTokenFilter.parseJwt(request);

        Map<String, Object> userInfo = jwtUtils.getClaimFromToken(token, claims -> {
            return claims;
        });
        int userid = Integer.parseInt(userInfo.get("id").toString());
        String email = userInfo.get("email").toString();
        String username = userInfo.get("username").toString();

        // SET EMAILĐTO
        emailDTO.setTemplate("mail-template");
        emailDTO.setSubject("Subject_TEST");
        emailDTO.setRecipient(email);

        //get user info from token then set to order
        order.setUserId(userid);
        order.setUsername(username);
        order.setEmail(email);
        order.setPaymentMethod("CASH");
        order.setStatus("CREATED");
        orderRepository.save(order);

        for (OrderLineForm orderLine : orderLines) {
            Optional<Product> maybeProduct = productRepository.findById(orderLine.getProductId());
            if (!maybeProduct.isPresent()) {
                response.setCode("001");
                response.setMessage("Product not found: " + orderLine.getProductId());
                return response;
            }
            // get product
            Product product = maybeProduct.get();

            // update quantity in db
            if (product.getQuantity() < orderLine.getQuantity()) {
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
        // Call API Send Mail
        String url = baseUrl + "/sendMail";
        props.put("fullName", username);
        props.put("product", orderLines);
        props.put("paymentMethod", order.getPaymentMethod());
        props.put("total", order.getTotalCost());
        props.put("status", order.getStatus());
        emailDTO.setProps(props);

        log.info("Resquest Body {}", CommonUtil.convertFromObject(emailDTO));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<EmailDTO> requestSaveProduct = new HttpEntity<>(emailDTO, headers);
        restTemplate.exchange(url, HttpMethod.POST, requestSaveProduct, new ParameterizedTypeReference<BaseResponse<Object>>() {
        });
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
        for (OrderItem orderItem : order.getItems()) {
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