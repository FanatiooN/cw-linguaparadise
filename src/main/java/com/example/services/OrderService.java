package com.example.services;


import com.example.dto.OrderCreateRequest;
import com.example.entities.Cours;
import com.example.entities.Order;
import com.example.entities.OrderItem;
import com.example.entities.User;
import com.example.repositories.OrderItemRepository;
import com.example.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService
{
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private UserService userService;
    private CoursService coursService;

    public Iterable<Order> getOrdersByUserId(long userId)
    {
        return orderRepository.findAllByUserId(userId);
    }

    public void saveOrderItem(OrderItem orderItem)
    {
        orderItemRepository.save(orderItem);
    }

    public void saveOrder(Order order)
    {
        orderRepository.save(order);
    }

    @Transactional
    public Order createOrderFromCart(User user, List<OrderCreateRequest.CourseInCart> courses, BigDecimal totalCost) {
        try {
            if (user.getBalance() == null || user.getBalance().compareTo(totalCost) < 0) {
                throw new RuntimeException("Недостаточно средств на балансе");
            }

            Order order = new Order();
            order.setUser(user);
            order.setTotalCost(totalCost.intValue());
            
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            order.setOrderDate(currentDate.format(formatter));
            
            for (OrderCreateRequest.CourseInCart courseInCart : courses) {
                Cours cours = coursService.findById(courseInCart.getId());
                if (cours != null) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setCours(cours);
                    order.addOrderItem(orderItem);
                }
            }
            
            order = orderRepository.save(order);
            
            BigDecimal newBalance = user.getBalance().subtract(totalCost);
            user.setBalance(newBalance);
            userService.save(user);
            
            log.info("Заказ успешно создан для пользователя: {} на сумму: {}₽", user.getUsername(), totalCost);
            
            return order;
            
        } catch (Exception e) {
            log.error("Ошибка создания заказа: ", e);
            throw new RuntimeException("Ошибка создания заказа: " + e.getMessage());
        }
    }
}
