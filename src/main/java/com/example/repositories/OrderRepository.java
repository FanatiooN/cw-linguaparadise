package com.example.repositories;

import com.example.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>
{
    Iterable<Order> findAllByUserId(long customUserId);
}
