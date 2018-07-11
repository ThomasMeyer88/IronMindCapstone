package com.ironmind.ferrus.controllers;

import com.ironmind.ferrus.Services.*;
import com.ironmind.ferrus.model.*;
import com.ironmind.ferrus.Services.programService;
import com.ironmind.ferrus.repositiories.Clients;


import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import com.ironmind.ferrus.repositiories.Programs;

import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.simplejavamail.mailer.config.TransportStrategy.SMTP_TLS;


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
        String emailAddress = client.getEmail();
        Email email = EmailBuilder.startingBlank()
                .from("Irondmind Notification", "ironmind2018@hotmail.com")
                .to("Noah", emailAddress)
                .withSubject(client.getName() + ", thank you for registering with Ironmind")
                .withHTMLText("<div style='text-align:center'><h2> Welcome to Ironmind </h2>" +
                        "<h3>Features</h3>" +
                        "<ul style='list-style: none'><li>Create a custom program to suit your needs</li>"
                        +"<li>Log your workouts</li>"+
                        "<li>View your progress overtime</li>"+
                        "<li>Find a coach to help guide your progress</li></ul>"+
                        "<a href='localhost:8080'>ironmind.app</a></div>")
                .buildEmail();

        MailerBuilder
                .withSMTPServer("smtp.live.com", 587, "ironmind2018@hotmail.com", "Finale1!")
                .withTransportStrategy(SMTP_TLS)
                .buildMailer()
                .sendMail(email);
        client.setPassword(hash);
        client.setRole("Client");
        clientDao.save(client);
        return "redirect:/client_login";
    }

    @GetMapping("/client_profile_page")
    public String clientPage(Model view){
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(clientSession.getRole());
        System.out.println(clientSession.getEmail());
        Client test = clientDao.findOne(clientSession.getId());
        clientSession.setRole(test.getRole());
        if (clientSession.getRole().equals("Coach")){
                view.addAttribute("client", clientSession);
                System.out.println("is a coach");
            List<Program> program = programDao.getPrograms().findAllByClient_Id(clientSession.getId());
                view.addAttribute("programs", program);
                List<Client> client = clientDao.findAllByCoachId(clientSession.getId());
                view.addAttribute("clients", client);
                return "coaches/coach_profile";
        }else{
            List<Program> program = programDao.getPrograms().findAllByClient_Id(clientSession.getId());
            view.addAttribute("programs", program);
            return "clients/client_profile_page";
            }
    }

    @RequestMapping(value = "/change_program", method = RequestMethod.POST)
    public String setActiveProgram(@RequestParam long program){
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientDao.findOne(clientSession.getId());
        Program activeProgram = programDao.getPrograms().findByClient_IdAndId(clientSession.getId(), program);
        client.setActiveprogram(activeProgram.getId());
        clientDao.save(client);
        return "clients/client_profile_page";
    }



    @GetMapping("/coach_profile")
    public String coachPage(Model view){
        return "coaches/coach_profile";
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
        view.addAttribute("selectId", Exercise);
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
    @PostMapping("/coach_profile/{progId}/{id}")
    public String assignProgram(@PathVariable long id, @PathVariable long progId){
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Program program = programDao.getPrograms().findById(progId);
        Program newProgram = new Program();
        newProgram.setProgramDays(program.getProgramDays());
        newProgram.setName(program.getName());
        newProgram.setClient(clientDao.findOne(id));
        programDao.getPrograms().save(newProgram);

        List<template> templates = tempDao.getTemplates().findAllByProgram_Id(progId);

        for(template temp: templates){
            template newTemp = new template();
            newTemp.setDay(newTemp.getDay());
            newTemp.setProgram(newProgram);
            tempDao.getTemplates().save(newTemp);
            List<WorkSet> workSets = workDao.getWork().findAllByTemplate(temp);

            for(WorkSet work: workSets){
                WorkSet wS = new WorkSet();
                wS.setTemplate(newTemp);
                wS.setExercise(work.getExercise());
                wS.setExerciseName(work.getExerciseName());
                workDao.getWork().save(wS);
                List<SubSet> sets = setDao.getSets().findAllByWorkSet_Id(work.getId());
                for(SubSet set: sets){
                    SubSet newSet = new SubSet();
                    newSet.setExerciseName(set.getExerciseName());
                    newSet.setReps(set.getReps());
                    newSet.setWeight(set.getWeight());
                    newSet.setWorkSet(wS);
                    setDao.getSets().save(newSet);
                }

            }
        }

        return "coaches/coach_profile";
    }


}
//    coach dashboard
//coach-client view
//        Add partials to views-nah
//        Create a program-
//        Have a list of programs
//        Have a list of clients
//        ---Be able to designate a program to a client
//        Each client view is everything except edit profile for that that client
