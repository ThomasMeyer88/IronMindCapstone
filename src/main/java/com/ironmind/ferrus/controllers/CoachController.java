package com.ironmind.ferrus.controllers;

import com.ironmind.ferrus.model.Client;
import com.ironmind.ferrus.repositiories.Clients;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CoachController {
    private Clients coachDao;
    private PasswordEncoder passwordEncoder;

    public CoachController(Clients coachDao, PasswordEncoder passwordEncoder) {
        this.coachDao = coachDao;
        this.passwordEncoder = passwordEncoder;


    }


    @GetMapping("/coaches_registration")
    public String showSignUpForm(Model view) {
        view.addAttribute("Coach", new Client());
        return "coaches/coaches_registration";
    }

    @PostMapping("/coaches_registration")
    public String saveCoach(@ModelAttribute Client coach) {
        String hash = passwordEncoder.encode(coach.getPassword());
        coach.setPassword(hash);
        coach.setRole("Coach");
        coachDao.save(coach);
        return "redirect:/client_login";

    }

    @GetMapping("/coach_profile/{id}/edit")
    public String showEditPage(@PathVariable Long id, Model view) {
        view.addAttribute("coach", coachDao.findOne(id));
        return "coaches/edit";
    }


    @PostMapping("/coach_profile/{id}/edit")
    public String updateProfile(@PathVariable Long id, @ModelAttribute Client coach) {
        Client coachSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        coachSession.setEmail(coach.getEmail());
        String hash = passwordEncoder.encode(coach.getPassword());
        coach.setPassword(hash);
        coachSession.setUsername(coach.getUsername());
        coachSession.setName(coach.getName());
        coachSession.setBio(coach.getBio());
        coachSession.setPhonenumber(coach.getPhonenumber());
        coachDao.save(coach);
        return "redirect:/coaches_profile";
    }

    @PostMapping("/coach_profile/{id}/delete")
    public String delete(@PathVariable long id) {
        coachDao.delete(id);
        return "redirect:/coach_profile";
    }

    @GetMapping("/coach_list")
    public String coachList(Model view) {
        List <Client> coaches = new ArrayList<>();
        List <Client> allUsers = coachDao.findAll();
        for(Client user: allUsers){
            if(user.getRole().equalsIgnoreCase("coach")){
                coaches.add(user);
            }
        }
        view.addAttribute("coachlist", coaches);
        return "clients/coach_list";
    }

}

