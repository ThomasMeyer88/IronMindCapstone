package com.ironmind.ferrus.controllers;

import com.ironmind.ferrus.model.Client;
import com.ironmind.ferrus.repositiories.Clients;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ClientController {
    private Clients clients;
    private PasswordEncoder passwordEncoder;

    public ClientController(Clients clients, PasswordEncoder passwordEncoder){
        this.clients = clients;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/register")
    public String showSignUpForm(Model model){
        model.addAttribute("client", new Client());
        return "clients/register";
    }

    @PostMapping("/register")
    public String saveClient(@ModelAttribute Client client){
        String hash = passwordEncoder.encode(client.getPassword());
        client.setPassword(hash);
        clients.save(client);
        return "redirect:/login";
    }

}