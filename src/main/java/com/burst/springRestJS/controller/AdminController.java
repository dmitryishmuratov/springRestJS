package com.burst.springRestJS.controller;

import com.burst.springRestJS.model.User;
import com.burst.springRestJS.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {
    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String all(@ModelAttribute("user") User user, Model model) {
        List<User> list = userService.findAll();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        model.addAttribute("list", json);
        return "admin";
    }

    @GetMapping("/personal")
    public String getUser(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("message", "Hello " + user.getUsername() + "!");
        model.addAttribute("user", user);
        return "personalAdmin";
    }

    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute("user") User user, Model model,
                          @ModelAttribute("role") String role) {
        userService.save(user);
        model.addAttribute("message", "Failed to register user!");
        return "redirect:/admin";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @ModelAttribute("role") String role, Model model) {
        userService.save(user);
        model.addAttribute("message", "Failed to update data");
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@ModelAttribute("id") Integer id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
