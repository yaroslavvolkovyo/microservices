package com.example.orderr_servicee.repository;

import com.example.orderr_servicee.dto.Status;
import com.example.orderr_servicee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.status FROM Order o WHERE o.orderId = :orderId")
    Status findStatusByOrderId(@Param("orderId")Long orderId);
}
