package com.ironmind.ferrus.controllers;

import com.ironmind.ferrus.Services.*;
import com.ironmind.ferrus.model.Client;
import com.ironmind.ferrus.model.CompletedSet;
import com.ironmind.ferrus.Services.programService;
import com.ironmind.ferrus.model.Exercise;
import com.ironmind.ferrus.model.Program;
import com.ironmind.ferrus.repositiories.Clients;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class ClientController {
    private Clients clientDao;
    private PasswordEncoder passwordEncoder;
    private ExerciseService exerciseService;
    private workSetService workDao;
    private templateService tempDao;
    private subSetService setDao;
    private programService programDao;
    private completedSetService compDao;

    public ClientController(Clients clientDao, PasswordEncoder passwordEncoder, ExerciseService exerciseService, workSetService workDao, templateService tempDao, subSetService setDao, programService programDao, completedSetService compDao) {
        this.clientDao = clientDao;
        this.passwordEncoder = passwordEncoder;
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
        clientDao.save(client);
        return "redirect:/client_login";
    }

    @GetMapping("/client_profile_page")
    public String clientPage(Model view){

        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        view.addAttribute("client", clientSession);
        return "clients/client_profile_page";
    }

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

    @RequestMapping(value = "/client_progress/{id}", method = RequestMethod.POST)
    public String switchProgress(@PathVariable long id, @RequestParam long exercise){
        return "redirect:/client_progress/" + id + "/" + exercise;
    }

    @GetMapping("/client_progress/{id}/{exercise}")
    public String viewSwitchProgress(@PathVariable long id, long exercise, Model view){
        List<CompletedSet> completedSets = compDao.getCompSets().findAllByExerciseIdAndClient_Id(exercise,id);
        List<Exercise> exercises = exerciseService.getExercises().findAll();
        view.addAttribute("exerciseList", exercises);
        view.addAttribute("sets", completedSets);
        return "clients/client_progress";
    }

    @GetMapping("/client_profile_page/create_program")
    public String viewCreateProgram(Model view){

        Program create = new Program("enter name here", new Client());
        create.setProgramDays(0);
        view.addAttribute("program", create);
        return "clients/create_program";
    }

    @PostMapping("/client_profile_page/create_program")
    public String CreateProgram(Program program){
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        program.setClient(clientSession);
        program.setName(program.getName());
        program.setProgramDays(program.getProgramDays());
        programDao.getPrograms().save(program);
        return "redirect:/client_profile_page";
    }


}