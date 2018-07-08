package com.ironmind.ferrus.controllers;

import com.ironmind.ferrus.Services.*;
import com.ironmind.ferrus.model.Client;
import com.ironmind.ferrus.model.CompletedSet;
import com.ironmind.ferrus.model.Exercise;
import com.ironmind.ferrus.model.programService;
import com.ironmind.ferrus.repositiories.ClientRepositories;
import com.ironmind.ferrus.repositiories.Clients;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ClientController {
    private Clients clients;
    private PasswordEncoder passwordEncoder;
    private final ClientRepositories clientDao;
    private ExerciseService exerciseService;
    private workSetService workDao;
    private templateService tempDao;
    private subSetService setDao;
    private programService programDao;
    private completedSetService compDao;


    public ClientController(Clients clients, PasswordEncoder passwordEncoder, ClientRepositories clientDao, ExerciseService exerciseService, workSetService workDao, templateService tempDao, subSetService setDao, programService programDao, completedSetService compDao) {
        this.clients = clients;
        this.passwordEncoder = passwordEncoder;
        this.clientDao = clientDao;
        this.exerciseService = exerciseService;
        this.workDao = workDao;
        this.tempDao = tempDao;
        this.setDao = setDao;
        this.programDao = programDao;
        this.compDao = compDao;
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

    @GetMapping("/client_profile_page")
    public String clientPage(Model view){

        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        view.addAttribute("client", clientSession);
        return "clients/client_profile_page";
    }

//    @GetMapping("/client_profile_page/{id}")
//    public String showProfile(@PathVariable long id, Model model){
//        model.addAttribute("Client", clientDao.findOne(id));
//        return "clients/client_profile_page";
//    }

    @GetMapping("/client_profile_page/{id}/edit")
    public String viewEdit(@PathVariable long id, Model model){
        model.addAttribute("client", clientDao.findOne(id));
        return "clients/edit";
    }

    @PostMapping("/client_profile_page/{id}/edit")
    public String updateProfile(@PathVariable long id, @ModelAttribute Client client){
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        clientSession.setEmail(client.getEmail());
        String hash = passwordEncoder.encode(client.getPassword());
        client.setPassword(hash);
//        clientSession.setPassword(client.getPassword());
        clientSession.setUsername(client.getUsername());
        clientSession.setName(client.getName());
        clientDao.save(client);
        return "redirect:/client_profile_page/";
    }

    @PostMapping("/client_profile_page/{id}/delete")
    public String delete(@PathVariable long id){
        clientDao.delete(id);
        return "redirect:/client_profile_page";
    }

    @GetMapping("/client_progress/{id}")
    public String viewProgress(@PathVariable long id, Model view){
        List<CompletedSet> completedSets = compDao.getCompSets().findAllByExerciseIdAndClient_Id(1, id);
        List<Exercise> exercises = exerciseService.getExercises().findAll();
        view.addAttribute("exerciseList", exercises);
        view.addAttribute("sets", completedSets);
        return "clients/client_progress";
    }

}