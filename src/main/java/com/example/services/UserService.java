package com.example.services;


import com.example.dto.AvatarUpdateRequest;
import com.example.dto.BalanceUpdateRequest;
import com.example.dto.ProfileUpdateRequest;
import com.example.entities.User;
import com.example.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(User user)
    {
        if (!userRepository.existsByUsername(user.getUsername()))
        {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getRole() == null) {
                user.setRole(com.example.entities.Role.USER);
            }
            userRepository.save(user);
        }
    }

    public void save(User user)
    {
        userRepository.save(user);
    }

    public List<User> findAll()
    {
        Iterable<User> allUsers = userRepository.findAll();
        List<User> users = new ArrayList<>();
        for (User user : allUsers)
        {
                users.add(user);
        }
        return users;
    }

    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }


    public User findUserById(Long id)
    {
        return userRepository.findUserById(id);
    }

    public User updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = findUserById(userId);
        if (user != null) {
            user.setName(request.getName());
            user.setSurName(request.getSurName());
            user.setBirthDate(request.getBirthDate());
            return userRepository.save(user);
        }
        return null;
    }

    public User updateBalance(Long userId, BalanceUpdateRequest request) {
        User user = findUserById(userId);
        if (user != null && request.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO;
            user.setBalance(currentBalance.add(request.getAmount()));
            return userRepository.save(user);
        }
        return null;
    }

    public User updateAvatar(Long userId, AvatarUpdateRequest request) {
        User user = findUserById(userId);
        if (user != null) {
            user.setAvatar(request.getAvatar());
            return userRepository.save(user);
        }
        return null;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
