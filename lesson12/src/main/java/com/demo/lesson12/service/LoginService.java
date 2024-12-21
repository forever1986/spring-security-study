package com.demo.lesson12.service;


import com.demo.lesson12.entity.LoginDTO;
import com.demo.lesson12.result.Result;

public interface LoginService {

    Result<String> login(LoginDTO loginDTO);

    Result<String> loginPhoneCode(LoginDTO loginDTO);

    Result<String> logout();
}
