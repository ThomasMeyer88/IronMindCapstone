package com.ironmind.ferrus.controllers;



import com.ironmind.ferrus.Services.ExerciseService;
import com.ironmind.ferrus.Services.subSetService;
import com.ironmind.ferrus.Services.templateService;
import com.ironmind.ferrus.Services.workSetService;
import com.ironmind.ferrus.model.*;
import com.ironmind.ferrus.repositiories.ClientRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class ExerciseController {
    private ExerciseService exerciseService;
    private workSetService workDao;
    private templateService tempDao;
    private subSetService setDao;
    private programService programDao;
    private ClientRepositories clientDao;

    @Autowired
    public ExerciseController
            (ExerciseService exerciseService, workSetService work, templateService tempDao,
             subSetService setDao, programService programService, ClientRepositories clientDao){
        this.exerciseService = exerciseService;
        this.workDao = work;
        this.tempDao = tempDao;
        this.setDao = setDao;
        this.programDao = programService;
        this.clientDao = clientDao;

    }

    @GetMapping("/tests")
    public String tests(Model view){
        List<WorkSet> workSets = workDao.getWork().findAll();
        for (WorkSet workSet : workSets) {
            System.out.println(workSet.getId());
            List<SubSet> subSets = workSet.getSubSets();
            for (SubSet subSet : subSets) {
                System.out.println("Subset is here" + subSet.getId());
            }
        }
        return("posts/index");
    }

    @GetMapping("/exercises/{name}/{day}")
    public String exercisesIndex(@PathVariable int day, @PathVariable String name, Model view){
        template temp = tempDao.getTemplates().findByProgram_IdAndDay(3, day);
        List<WorkSet> daySet = workDao.getWork().findAllByTemplate(temp);
        System.out.println(daySet.size());
        SubSet subSet = new SubSet(0, 0, " ", null);
        view.addAttribute("workSets", daySet);
        view.addAttribute("subSet", subSet);
        view.addAttribute("exercises", exerciseService.getExercises().findAll());
        view.addAttribute("day", day);
        view.addAttribute("name", name);
        return("exercises/exercises");
    }

    @GetMapping("/newexercise")
    public String create(Model view){
        Exercise exercise = new Exercise(" ", " ");
        view.addAttribute("exercise", exercise);
        return("exercises/newExercise");

    }

    @PostMapping("/newexercise")
    public String createExercise(Exercise exercise){
        System.out.println(exercise);
        exerciseService.getExercises().save(exercise);
        return "redirect:/exercises";
    }

    @PostMapping("/createplan/{name}/{day}")
    public String createDay(@PathVariable long day, @PathVariable String name, SubSet subSet){
        System.out.println(subSet.getExerciseName());
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            try {
                Program program = programDao.getPrograms().findByClient_IdAndName(clientSession.getId(), name);
                System.out.println("First try program " + program.getId());
                template temp = tempDao.getTemplates().findByProgram_IdAndDay(program.getId(), (int)day);
                System.out.println("First try temp " + temp.getId());
                WorkSet work = workDao.getWork().findByTemplate_IdAndExerciseName(temp.getId(), subSet.getExerciseName());
                work.setTemplate(temp);
                System.out.println("Work ID AND TEMPLATE");
                System.out.println(work.getId() + " " + work.getTemplate());
                subSet.setWorkSet(work);
                setDao.getSets().save(subSet);
                System.out.println(subSet.getWorkSet().getTemplate());
                return "redirect:/exercises/" + name + "/" + day;
            } catch (NullPointerException e) {
                Program program = programDao.getPrograms().findByClient_IdAndName(clientSession.getId(), name);
                System.out.println("First try program " + program.getId());
                template temp = tempDao.getTemplates().findByProgram_IdAndDay(program.getId(), (int)day);
                System.out.println("First try temp " + temp.getId());
                WorkSet work = new WorkSet(subSet.getExerciseName(), exerciseService.getExercises().findByName(subSet.getExerciseName()));
                work.setTemplate(temp);
                workDao.getWork().save(work);
                subSet.setWorkSet(work);
                setDao.getSets().save(subSet);
                return "redirect:/exercises/" + name + "/" + day;
            }

        } catch (NullPointerException e){
            template temp = new template((int)day);
            temp.setProgram(programDao.getPrograms().findByClient_IdAndName(clientSession.getId(), name));
            tempDao.getTemplates().save(temp);
            WorkSet work = new WorkSet(subSet.getExerciseName(),exerciseService.getExercises().findByName(subSet.getExerciseName()));
            work.setTemplate(temp);
            workDao.getWork().save(work);
            subSet.setWorkSet(work);
            setDao.getSets().save(subSet);
            return "redirect:/exercises/" + name + "/" + day;
        }
    }

    @RequestMapping(value = "/editplan/{day}", method = RequestMethod.POST)
    public String getTest(@PathVariable long day, @RequestParam int id, @RequestParam int weight, @RequestParam int reps,
                          @RequestParam int sets) {


        return "redirect:/exercises/" + day;
    }


}