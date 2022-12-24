package com.taxi.controller;

import com.taxi.entity.User;
import com.taxi.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }

    @PostMapping({"/registerNewUser"})
    public ResponseEntity<Object> registerNewUser(@RequestBody User user, @RequestParam String role) {
        try {
            var res = userService.registerNewUser(user, role);
            return ResponseEntity
                    .ok(res);
        } catch (NotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
