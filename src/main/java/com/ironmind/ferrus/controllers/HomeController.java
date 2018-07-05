package com.ironmind.ferrus.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    @GetMapping("/")
    public String home(){
        return "clients/landing";
    }
}

//

