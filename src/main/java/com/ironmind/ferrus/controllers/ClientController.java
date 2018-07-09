package com.ironmind.ferrus.controllers;

import com.ironmind.ferrus.Services.*;
import com.ironmind.ferrus.model.Client;
import com.ironmind.ferrus.model.CompletedSet;
import com.ironmind.ferrus.Services.programService;
import com.ironmind.ferrus.model.Exercise;
import com.ironmind.ferrus.model.Program;
import com.ironmind.ferrus.repositiories.Clients;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        List<Exercise> exercises = exerciseService.getExercises().findAll();
        view.addAttribute("exerciseList", exercises);
        return "clients/client_progress";
    }

    @RequestMapping(value = "/client_progress/{id}", method = RequestMethod.POST)
    public String switchProgress(@PathVariable long id, @RequestParam long exerciseId){
        System.out.println("Id = " + exerciseId);
        return "redirect:/client_progress/" + id + "/" + exerciseId;
    }

    @GetMapping("/client_progress/{id}/{Exercise}")
    public String viewSwitchProgress(@PathVariable long id, @PathVariable long Exercise, Model view){
        List<CompletedSet> maxes = new ArrayList<>();
        List<CompletedSet> volume = new ArrayList<>();
        List<CompletedSet> absMax = compDao.getCompSets().findAllByExerciseIdAndClient_IdOrderByEstimated1RMDesc(Exercise, id);

        for(long i = 0; i < 180; i++){
            long volSum = 0;
            long repSum = 0;
            long intensity = 0;
            long intSum = 0;
            List<CompletedSet> max = compDao.getCompSets().findAllByClient_IdAndExerciseIdAndDayOrderByEstimated1RMDesc(id, Exercise, i);
            for(int x = 0; x < max.size(); x++){
                volSum += max.get(x).getTotalweight();
                repSum += max.get(x).getReps();
                intensity = (max.get(x).getEstimated1RM()*100)/absMax.get(0).getEstimated1RM();
                intSum += intensity;
            }
            try {
                CompletedSet volSet = new CompletedSet(max.get(0).getDay(), max.get(0).getExerciseId(), volSum, max.get(0).getEstimated1RM());

                if(max.size() > 0) {
                    intensity = intSum / max.size();
                    System.out.println(intensity);
                } else {
                    intensity = 0;
                }
                    volSet.setIntensity(intensity);



                volSet.setReps(repSum);
                volume.add(volSet);

                maxes.add(max.get(0));
                max.clear();
            } catch (IndexOutOfBoundsException e){

            }
        }

        List<Exercise> exercises = exerciseService.getExercises().findAll();
        view.addAttribute("exerciseList", exercises);
        view.addAttribute("sets", volume);
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