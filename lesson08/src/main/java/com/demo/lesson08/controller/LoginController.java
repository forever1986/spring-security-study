package com.demo.lesson08.controller;

import com.demo.lesson08.entity.LoginDTO;
import com.demo.lesson08.result.Result;
import com.demo.lesson08.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        return loginService.login(loginDTO, request, response);
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        return loginService.logout();
    }

}
