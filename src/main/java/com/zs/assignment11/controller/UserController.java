package com.zs.assignment11.controller;

import com.zs.assignment11.model.User;
import com.zs.assignment11.service.UserService;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Observed(name = "user.controller", contextualName = "User Controller")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser")
    public ResponseEntity<Map<String,String>> handleGetUser(){
        long start = System.currentTimeMillis();
        log.debug("/getUser endpoint was called");
        List<User> users=userService.getAllUsers();
        long endTime = System.currentTimeMillis();
        Map<String,String> response=new LinkedHashMap<>();
        response.put("status","success");
        response.put("message","All user fetched successfully");
        response.put("time",(endTime-start)+"ms");
        response.put("users:",users.toString());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/addUser")
    public ResponseEntity<Map<String, String>> handleAddUser(@RequestBody User user) {
        long startTime = System.currentTimeMillis();
        log.debug("/addUser endpoint was called with user: {}", user.getUsername());
        User newUser = userService.addUser(user);
        long endTime = System.currentTimeMillis();
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "User added successfully with username: " + user.getUsername());
        response.put("time", (endTime - startTime) + "ms");
        response.put("User", newUser.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
