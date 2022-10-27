package com.vanthan.vn.repository;

import com.vanthan.vn.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("select o from Order o where o.userId = :userId ")
    List<Order> findOrderByUserId(@Param("userId")int userId);
}
