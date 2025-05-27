package com.example.controllers;

import com.example.entities.Cours;
import com.example.entities.NumberOfItems;
import com.example.entities.Order;
import com.example.entities.OrderItem;
import com.example.security.CustomUserDetails;
import com.example.services.CoursService;
import com.example.services.OrderService;
import com.example.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CoursService coursService;
    @Autowired
    private UserService userService;

    public OrderController(OrderService orderService, CoursService coursService)
    {
        this.orderService = orderService;
        this.coursService = coursService;
    }

    @GetMapping("/")
    public String getStart()
    {
        return "redirect:person";
    }

    @GetMapping("/courses")
    public String getCatalog(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        model.addAttribute("coursList", coursService.findAll());
        
        if (customUserDetails != null) {
            model.addAttribute("user", customUserDetails.getUser());
        }
        
        return "courses";
    }

    @GetMapping("/person")
    public String getAccount(Model model , @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        Long id = customUserDetails.getUser().getId();
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("orders", orderService.getOrdersByUserId(id));
        return "person";
    }

    @GetMapping("/korzina")
    public String getKorzina(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        if (customUserDetails != null) {
            model.addAttribute("user", customUserDetails.getUser());
        }
        
        return "korzina";
    }

    @PostMapping("/add-order")
    public String addNewOrder(@ModelAttribute Order order, @ModelAttribute OrderItem orderItems,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        order.setOrderDate(currentDate.format(formatter));
        order.setUser(customUserDetails.getUser());
        
        for (OrderItem orderItem : order.getOrderItems())
        {
            orderService.saveOrderItem(orderItem);
        }
        orderService.saveOrder(order);
        
        log.info("Заказ создан для пользователя: {} на сумму: {}₽", 
                order.getUser().getUsername(), order.getTotalCost());
        
        return "redirect:person";
    }

}
