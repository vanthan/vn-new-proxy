package com.vanthan.vn.repository;

import com.vanthan.vn.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
    public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("select o from Order o where o.userId = :userId ")
    List<Order> findOrderByUserId(@Param("userId")int userId);
    @Modifying
    @Transactional
    @Query("update Order o set o.totalCost = :#{#totalCost}, o.totalItems = :#{#totalItems} where o.id = :id")
    void updateOrderById(@Param("id")int id,@Param("totalCost") int totalCost, @Param("totalItems") int totalItems);
}
