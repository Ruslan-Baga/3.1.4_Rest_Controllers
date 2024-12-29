package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String allUsers(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("users", userService.findAll());
        String email = userDetails.getUsername();
        model.addAttribute("userEmail", email);
        User user = userService.findByEmail(email);
        model.addAttribute("role", user.getRolesAsString());
        return "/admin";
    }
    @GetMapping("/new")
    public String newUserPage(Model model, Principal principal) {
        model.addAttribute("userEmail", principal.getName());
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("role", user.getRolesAsString());
        return "admin";
    }

    @PostMapping("/newUser")
    public String create(@Valid @ModelAttribute("user") User user,BindingResult bindingResult, @RequestParam String role) {
        if (bindingResult.hasErrors()){
            return "/views/new";
        }
        try {
            userService.save(user, role);
        } catch (RuntimeException e) {
            bindingResult.rejectValue("email", "error.user", e.getMessage());
            return "/views/new";
        }

        return "redirect:/admin";
    }
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "views/delete";
    }
    @PostMapping("/delete")
    public String delete(@ModelAttribute("user") User user){
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }
    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            model.addAttribute("users", userService.findAll());
            return "/admin";
        }
        try {
            userService.updateUser(user, user.getId());
        } catch (RuntimeException e) {
            bindingResult.rejectValue("email", "error.user", e.getMessage());
            model.addAttribute("users", userService.findAll());
            model.addAttribute("user", user);
            return "/admin";
        }
        return "redirect:/admin";
    }
}