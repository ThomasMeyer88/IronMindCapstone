package com.ironmind.ferrus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "clients/landing";
    }


    @GetMapping("/about")
    public String about(){
        return "coaches/about";
    }

}

