package com.demo.lesson05.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoAnnotationController {

    @GetMapping("/infowithannotation")
    @PreAuthorize("info") // 需要具备info权限才能方法
    public String info() {
        return"info";
    }

}
