package com.demo.lesson11.controller;

import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/get1")
    @CrossOrigin
    public String get1() {
        return"get1";
    }

    @GetMapping("/get2")
    public String get2() {
        return"get2";
    }

    @PostMapping("/post1")
    public String post1() {
        return"post1";
    }

    @PostMapping("/post2")
    public String post2() {
        return"post2";
    }

}
