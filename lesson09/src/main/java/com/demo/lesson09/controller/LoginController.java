package com.demo.lesson09.controller;

import com.demo.lesson09.entity.LoginDTO;
import com.demo.lesson09.result.Result;
import com.demo.lesson09.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        return loginService.login(loginDTO);
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        return loginService.logout();
    }

}
