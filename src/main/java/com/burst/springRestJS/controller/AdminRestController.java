package com.burst.springRestJS.controller;

import com.burst.springRestJS.model.User;
import com.burst.springRestJS.model.dto.UserDTO;
import com.burst.springRestJS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/api")
public class AdminRestController {
    private UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        List<User> list = userService.findAll();
        if (list.isEmpty()) {
            return (ResponseEntity<List<User>>) ResponseEntity.noContent();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Integer id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody UserDTO user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        userService.initRole(newUser, user.getRole());
        userService.save(newUser);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/edit")
    public ResponseEntity<User> editUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.editUser(user));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        this.userService.deleteById(id);
    }
}
