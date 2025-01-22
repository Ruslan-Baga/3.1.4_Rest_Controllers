package ru.kata.spring.boot_security.demo.initMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class NewUserToDB {
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;

    @Autowired
    public NewUserToDB(UserServiceImpl userServiceImpl, UserRepository userRepository) {
        this.userServiceImpl = userServiceImpl;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Role role = new Role("ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = new User("Admin", "Local", 25, "admin@gmail.com", "root", roles);
        if (!userRepository.existsByEmail(user.getEmail())) {
            userServiceImpl.save(user);
        }
    }
}
