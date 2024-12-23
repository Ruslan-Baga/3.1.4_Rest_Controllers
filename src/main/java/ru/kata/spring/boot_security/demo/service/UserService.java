package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


public interface UserService extends UserDetailsService {
    List<User> findAll();

    void save(User user, String role);

    void deleteUser(int id);

    User getUser(int id);

    void updateUser(User user, int id);
    User findByEmail(String email);

}
