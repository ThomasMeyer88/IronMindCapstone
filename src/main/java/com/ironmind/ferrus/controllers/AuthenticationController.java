package com.ironmind.ferrus.controllers;

import com.ironmind.ferrus.model.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {


    @GetMapping("/client_login")
    public String showLoginForm(Model view){
        Client client = new Client(" ", " ");
        view.addAttribute("client", client);
        return"clients/client_login";}
}






