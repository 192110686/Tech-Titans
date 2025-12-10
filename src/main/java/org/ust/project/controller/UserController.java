package org.ust.project.controller;

import org.ust.project.model.User;
import org.ust.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    
    // Simple login check (Returns user if password matches, else null)
    // NOTE: For a real project, use Spring Security. This is just for basic testing.
    @PostMapping("/login")
    public User loginUser(@RequestBody User loginDetails) {
        return userService.findByUsername(loginDetails.getUsername())
            .filter(user -> user.getPassword().equals(loginDetails.getPassword()))
            .orElse(null);
    }
}