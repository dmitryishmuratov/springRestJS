package com.burst.springRestJS.service;

import com.burst.springRestJS.model.Role;
import com.burst.springRestJS.model.User;
import com.burst.springRestJS.model.dto.UserDTO;
import com.burst.springRestJS.repository.RoleRepo;
import com.burst.springRestJS.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private BCryptPasswordEncoder passwordEncoder;

    private Role roleAdmin;
    private Role roleUser;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public void save(User user) {
        if (notEmpty(user.getUsername(), user.getEmail(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
        }
    }

    @Override
    public void deleteById(Integer id) {
        userRepo.deleteById(id);
    }

    @Override
    public User findById(Integer id) {
        User result = userRepo.getById(id);
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    public User findByUsername(String username) {
        User result = userRepo.findByUsername(username);
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    public Role getRoleById(Integer id) {
        return roleRepo.getRoleById(id);
    }

    private boolean notEmpty(String username, String email, String password) {
        return username != null && !username.isEmpty() &&
                email != null && !email.isEmpty() &&
                password != null && !password.isEmpty();
    }

    private boolean isEmpty(User user) {
        User user1 = userRepo.findByUsername(user.getUsername());
        if (user1 != null) {
            return true;
        } else {
            return false;
        }
    }

    private void addRoleAdmin(User user) {
        if (roleAdmin == null) {
            roleAdmin = roleRepo.getRoleById(2);
        }
        user.getRoles().clear();
        user.getRoles().add(roleAdmin);
    }

    private void addRoleUser(User user) {
        if (roleUser == null) {
            roleUser = roleRepo.getRoleById(1);
        }
        user.getRoles().clear();
        user.getRoles().add(roleUser);
    }

    @Override
    public void initRole(User user, String role) {
        if (role.equalsIgnoreCase("admin")) {
             addRoleAdmin(user);
        } else {
             addRoleUser(user);
        }
    }

    public User editUser(UserDTO user) {
        User updateUser = userRepo.getById(user.getId());
        updateUser.setUsername(user.getUsername());
        updateUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        initRole(updateUser, user.getRole());
        userRepo.save(updateUser);
        return updateUser;
    }

    @Override
    public void registration(User user) {
        if (!isEmpty(user)) {
            addRoleUser(user);
            save(user);
        }
        return;
    }
}
