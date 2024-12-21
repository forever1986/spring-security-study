package com.demo.lesson12.controller;

import com.demo.lesson12.entity.LoginDTO;
import com.demo.lesson12.result.Result;
import com.demo.lesson12.service.LoginService;
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

    @PostMapping("/loginphone")
    public Result<String> loginPhone(@RequestBody LoginDTO loginDTO) {
        return loginService.loginPhoneCode(loginDTO);
    }

    @PostMapping("/logout")
    public Result<String> logout() {
        return loginService.logout();
    }

}
