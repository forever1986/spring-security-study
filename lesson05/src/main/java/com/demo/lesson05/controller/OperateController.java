package com.demo.lesson05.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operate")
public class OperateController {

    @GetMapping("/info")
    public String info() {
        return"info";
    }

    @GetMapping("/operate")
    public String operate() {
        return"operate";
    }

    @GetMapping("/manager")
    public String manager() {
        return"manager";
    }

}
