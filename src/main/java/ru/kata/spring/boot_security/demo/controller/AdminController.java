package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/views/list";
    }
    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "views/new";
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

        return "redirect:/admin/users";
    }
    @GetMapping("/deleteUser")
    public String deleteUser(){
        return "views/delete";
    }
    @PostMapping("/delete")
    public String delete(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "There is no user with this id: " + id);
            return "redirect:/admin/deleteUser";
        }

        return "redirect:/admin/users";
    }
    @GetMapping("/editUser")
    public String editUser(@RequestParam int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "views/edit";
    }
    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "views/edit";
        }
        try {
            userService.updateUser(user, user.getId());
        } catch (RuntimeException e) {
            bindingResult.rejectValue("email", "error.user", e.getMessage());
            return "/views/edit";
        }


        return "redirect:/admin/users";
    }
    @GetMapping()
    public String adminPage(){
        return "/admin";
    }
}