package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.CustomUserDetails;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void save(User user, String role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roleEntity = roleRepository.findByRole(role);
        if (roleEntity == null) {
            roleEntity = new Role(role);
            roleRepository.save(roleEntity);
        }
        user.addRole(roleEntity);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("A user with this email already exists");
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public void updateUser(User user, int id) {
        if (userRepository.existsByEmailAndIdNot(user.getEmail(), id)) {
            throw new RuntimeException("A user with this email already exists");
        }
        User updateUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        updateUser.setAge(user.getAge());
        updateUser.setFirstName(user.getFirstName());
        updateUser.setEmail(user.getEmail());
        updateUser.setPassword(user.getPassword());
        updateUser.setLastName(user.getLastName());
        updateUser.setRoleUser(user.getRoleUser());
        Role roleEnt = roleRepository.findByRole(user.getRole());
        if (roleEnt == null) {
            roleEnt = new Role(user.getRole());
            roleRepository.save(roleEnt);
        }
        updateUser.getRoleUser().clear();
        updateUser.addRole(roleEnt);

        userRepository.save(updateUser);
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Hibernate.initialize(user.getRoleUser());
        return new CustomUserDetails(user);
    }

}

