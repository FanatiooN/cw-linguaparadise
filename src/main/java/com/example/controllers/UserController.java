package com.example.controllers;

import com.example.dto.AvatarUpdateRequest;
import com.example.dto.BalanceUpdateRequest;
import com.example.dto.CourseCreateRequest;
import com.example.dto.OrderCreateRequest;
import com.example.dto.ProfileUpdateRequest;
import com.example.entities.Cours;
import com.example.entities.Order;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.security.CustomUserDetails;
import com.example.services.CoursService;
import com.example.services.OrderService;
import com.example.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class UserController
{
    private UserService userService;
    private CoursService coursService;
    private OrderService orderService;

    @GetMapping("login")
    public String getRegistration(Model model)
    {
        model.addAttribute("userReg", new User());
        System.out.println("getRegistration из User");
        return "login";
    }

    @PostMapping("reg")
    public String addNewUser(@ModelAttribute("userReg") User user)
    {
        userService.addUser(user);

        return "redirect:login";
    }

    @PostMapping("/api/profile/update")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long userId = userDetails.getUser().getId();
            User updatedUser = userService.updateProfile(userId, request);
            
            if (updatedUser != null) {
                return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Профиль успешно обновлен\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Ошибка обновления профиля\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/api/balance/update")
    @ResponseBody
    public ResponseEntity<?> updateBalance(@RequestBody BalanceUpdateRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long userId = userDetails.getUser().getId();
            User updatedUser = userService.updateBalance(userId, request);
            
            if (updatedUser != null) {
                return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Баланс успешно пополнен\", \"newBalance\": " + updatedUser.getBalance() + "}");
            } else {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Ошибка пополнения баланса\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/api/avatar/update")
    @ResponseBody
    public ResponseEntity<?> updateAvatar(@RequestBody AvatarUpdateRequest request,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Long userId = userDetails.getUser().getId();
            User updatedUser = userService.updateAvatar(userId, request);
            
            if (updatedUser != null) {
                return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Аватар успешно обновлен\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Ошибка обновления аватара\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/api/courses/create")
    @ResponseBody
    public ResponseEntity<?> createCourse(@RequestBody CourseCreateRequest request,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails.getUser().getRole() != Role.ADMIN) {
                return ResponseEntity.status(403).body("{\"success\": false, \"message\": \"Доступ запрещен\"}");
            }

            Cours newCourse = coursService.createCourse(request);
            
            if (newCourse != null) {
                return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Курс успешно создан\", \"courseId\": " + newCourse.getId() + "}");
            } else {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Ошибка создания курса\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/api/orders/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderCreateRequest request, 
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userDetails.getUser();
            Order order = orderService.createOrderFromCart(user, request.getCourses(), request.getTotalCost());
            
            response.put("success", true);
            response.put("message", "Заказ успешно создан");
            response.put("orderId", order.getId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/api/user/balance")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserBalance(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = userDetails.getUser().getId();
            User user = userService.findUserById(userId);
            response.put("balance", user.getBalance() != null ? user.getBalance().intValue() : 0);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("balance", 0);
            return ResponseEntity.ok(response);
        }
    }
}
