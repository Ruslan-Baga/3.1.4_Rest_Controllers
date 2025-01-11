package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;


public interface UserService extends UserDetailsService {
    List<User> findAll();

    void save(User user);

    void deleteUser(int id);

    User getUser(int id);

    void updateUser(User user);
    User findByEmail(String email);
    List<Role> allRoles();
}
