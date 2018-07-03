package com.ironmind.ferrus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    @GetMapping("/client_login")
    public String showLoginForm(){return"clients/client_login";}
}
