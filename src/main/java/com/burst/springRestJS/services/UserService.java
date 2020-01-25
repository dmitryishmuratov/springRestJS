package com.burst.springRestJS.services;

import com.burst.springRestJS.model.Role;
import com.burst.springRestJS.model.User;
import com.burst.springRestJS.model.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<User> findAll();
    void save(User user);
    void deleteById(Integer id);
    User findById(Integer id);
    User findByUsername(String username);
    Role getRoleById(Integer id);
    void initRole(User user, String role);
    User editUser(UserDTO user);
    void registration(User user);
}
