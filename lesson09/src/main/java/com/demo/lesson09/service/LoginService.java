package com.demo.lesson09.service;


import com.demo.lesson09.entity.LoginDTO;
import com.demo.lesson09.result.Result;

public interface LoginService {

    Result<String> login(LoginDTO loginDTO);

    Result<String> logout();
}
