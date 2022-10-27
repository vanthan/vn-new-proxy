package com.vanthan.vn.repository;

import com.vanthan.vn.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrderDetailRepository extends JpaRepository<OrderItem, Integer> {
    Optional<OrderItem> findById(Integer id);
}
