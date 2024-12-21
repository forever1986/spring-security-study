package com.demo.lesson12.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public String demo() {
        return"demo";
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PRODUCT_LIST')")
    public String list() {
        return"list";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasAuthority('PRODUCT_EDIT')")
    public String edit() {
        return"edit";
    }

}
