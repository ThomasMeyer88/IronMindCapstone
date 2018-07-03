package com.ironmind.ferrus.controllers;

import com.ironmind.ferrus.model.Client;
import com.ironmind.ferrus.repositiories.ClientRepositories;
import com.ironmind.ferrus.repositiories.Clients;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ClientController {
    private Clients clients;
    private PasswordEncoder passwordEncoder;
    private final ClientRepositories clientDao;

    public ClientController(Clients clients, PasswordEncoder passwordEncoder, ClientRepositories clientDao){
        this.clients = clients;
        this.passwordEncoder = passwordEncoder;
        this.clientDao = clientDao;
    }


    @GetMapping("/client_registration")
    public String showSignUpForm(Model model){
        model.addAttribute("client", new Client());
        return "clients/client_registration";
    }

    @PostMapping("/client_registration")
    public String saveClient(@ModelAttribute Client client){
        String hash = passwordEncoder.encode(client.getPassword());
        client.setPassword(hash);
        clients.save(client);
        return "redirect:/client_login";
    }

    @GetMapping("/client_profile_page/{id}")
    public String showProfile(@PathVariable long id, Model model){
        model.addAttribute("Client", clientDao.findOne(id));
        return "clients/client_profile_page";
    }

    @GetMapping("/client_profile_page/{id}/edit")
    public String viewEdit(@PathVariable long id, Model model){
        model.addAttribute("Client", clientDao.findOne(id));
        return "clients/edit";
    }

    @PostMapping("/client_profile_page/{id}/edit")
    public String updateProfile(@PathVariable long id, @ModelAttribute Client client){
        clientDao.save(client);
        return "redirect:/client_profile_page";
    }





}