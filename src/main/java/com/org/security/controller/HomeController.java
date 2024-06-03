package com.org.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String defaultMethod(){
        return  "this is default api for everyone";
    }
    @GetMapping("/home")
    public String homeMethod(){
        return  "home api for everyone";
    }
    @GetMapping("/public/api1")
    public String publicMethod1(){
        return  "authenticated public api 1";
    }

    @GetMapping("/public/api2")
    public String publicMethod2(){
        return  "authenticated public api 2";
    }
    @GetMapping("/admin/api1")
    public String adminMethod1(){
        return  "authenticated admin api 1";
    }

    @GetMapping("/admin/api2")
    public String adminMethod2(){
        return  "authenticated admin api 2";
    }

    @PreAuthorize("ADMIN")
    @GetMapping("/private/api2")
    public String privateMethod(){
        return  "authenticated private api 2";
    }
}
