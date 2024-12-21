package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    void save(User user);

    void deleteUser(int id);

    User getUser(int id);

    void updateUser(User user, int id);

}
