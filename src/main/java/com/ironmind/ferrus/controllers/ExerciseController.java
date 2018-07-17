package com.ironmind.ferrus.controllers;


import com.ironmind.ferrus.Services.*;
import com.ironmind.ferrus.model.*;
import com.ironmind.ferrus.repositiories.Clients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ExerciseController {
    private ExerciseService exerciseService;
    private workSetService workDao;
    private templateService tempDao;
    private subSetService setDao;
    private programService programDao;
    private completedSetService compDao;
    private Clients clientDao;

    @Autowired
    public ExerciseController
            (ExerciseService exerciseService, workSetService work, templateService tempDao,
             subSetService setDao, programService programService, Clients clientDao,
             completedSetService compDao) {
        this.exerciseService = exerciseService;
        this.workDao = work;
        this.tempDao = tempDao;
        this.setDao = setDao;
        this.programDao = programService;
        this.clientDao = clientDao;
        this.compDao = compDao;

    }

    @GetMapping("/tests")
    public String tests(Model view) {
        List<WorkSet> workSets = workDao.getWork().findAll();
        for (WorkSet workSet : workSets) {
            System.out.println(workSet.getId());
            List<SubSet> subSets = workSet.getSubSets();
            for (SubSet subSet : subSets) {
                System.out.println("Subset is here" + subSet.getId());
            }
        }
        return ("posts/index");
    }

    @GetMapping("/logplan/{id}")
    public String loadLog(@PathVariable long id, Model view) {
        Client client = clientDao.findOne(id);
        Program program = programDao.getPrograms().findById(client.getActiveprogram());
        long progId = program.getId();
        view.addAttribute("progid", id);
        return "redirect:/log/" + progId + "/1";
    }

    @GetMapping("/log/{id}/{day}")
    public String dayLog(@PathVariable int day, @PathVariable long id, Model view) {
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Program program = programDao.getPrograms().findById(id);
        template temp = tempDao.getTemplates().findByProgram_IdAndDay(id, day);
        List<WorkSet> daySet = workDao.getWork().findAllByTemplate(temp);
        if (daySet.size() == 0) {
            view.addAttribute("done", "You've finished all of today's sets!");
        }
        List<CompletedSet> setsDone = compDao.getCompSets().findAllByClient_IdAndDayOrderByExerciseName(clientSession.getId(), (long) day);
        long totalReps = 0;
        long totalWeight = 0;
        long totalSets = setsDone.size();
        for(CompletedSet set: setsDone){
            totalReps += set.getReps();
            totalWeight += set.getReps();
        }
        view.addAttribute("totalReps", totalReps);
        view.addAttribute("totalWeight", totalWeight);
        view.addAttribute("totalSets", totalSets);
        view.addAttribute("setsDone", setsDone);
        view.addAttribute("workSets", daySet);
        view.addAttribute("name", program.getName());
        view.addAttribute("day", day);
        view.addAttribute("progId", id);
        return ("exercises/log");
    }



    @GetMapping("/log/{progId}/{day}/{id}")
    public String dayLogDropDown(@PathVariable int day, @PathVariable long progId, @PathVariable long id, Model view) {
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Program program = programDao.getPrograms().findById(progId);
        template temp = tempDao.getTemplates().findByProgram_IdAndDay(progId, day);
        List<WorkSet> daySet = workDao.getWork().findAllByTemplate(temp);
        if (daySet.size() == 0) {
            view.addAttribute("done", "You've finished all of today's sets!");
        }
        List<CompletedSet> setsDone = compDao.getCompSets().findAllByClient_IdAndDayOrderByExerciseName(clientSession.getId(), (long) day);
        long totalReps = 0;
        long totalWeight = 0;
        long totalSets = setsDone.size();
        for(CompletedSet set: setsDone){
            totalReps += set.getReps();
            totalWeight += set.getReps();
        }
        view.addAttribute("totalReps", totalReps);
        view.addAttribute("totalWeight", totalWeight);
        view.addAttribute("totalSets", totalSets);
        view.addAttribute("setsDone", setsDone);
        view.addAttribute("dropId", id);
        view.addAttribute("workSets", daySet);
        view.addAttribute("name", program.getName());
        view.addAttribute("day", day);
        view.addAttribute("progId", progId);

        return ("exercises/log");
    }

    @GetMapping("/exercises/{id}")
    public String loadProgram(@PathVariable long id, Model view) {
        Client client = clientDao.findOne(id);
        Program program = programDao.getPrograms().findById(client.getActiveprogram());
        long progId = program.getId();
        view.addAttribute("progId", id);
        return "redirect:/exercises/" + progId + "/1";
    }



    @GetMapping("/exercises/{id}/{day}")
    public String exercisesIndex(@PathVariable int day, @PathVariable long id, Model view) {
        Program program = programDao.getPrograms().findOne(id);
        template temp = tempDao.getTemplates().findByProgram_IdAndDay(id, day);

        //Program program = programDao.getPrograms().findByClient_IdAndName(clientSession.getId(), name);
        long days = program.getProgramDays();
        List<Long> progDays = new ArrayList<>();
        for (long i = 1; i <= days; i++) {
            progDays.add(i);
        }

        List<WorkSet> daySet = workDao.getWork().findAllByTemplate(temp);
        System.out.println(daySet.size());
        SubSet subSet = new SubSet(0, 0, " ", null);

        List<template> templates = tempDao.getTemplates().findAllByUsable(1);

        view.addAttribute("tempList", templates);
        view.addAttribute("days", progDays);
        view.addAttribute("workSets", daySet);
        view.addAttribute("subSet", subSet);
        view.addAttribute("exercises", exerciseService.getExercises().findAll());
        view.addAttribute("day", day);
        view.addAttribute("name", program.getName());
        view.addAttribute("progId", program.getId());
        return ("exercises/exercises");
    }

    @PostMapping("/addtemplate/{id}/{day}")
    public String addTemplate(@PathVariable long id, @PathVariable int day, @RequestParam long tempId){
        template copyTemp = tempDao.getTemplates().findOne(tempId);

        try{
            template baseTemp = tempDao.getTemplates().findByProgram_IdAndDay(id, day);
            System.out.println("1st Try temp id and day are " + baseTemp.getId() + baseTemp.getDay());
            tempDao.getTemplates().save(baseTemp);
            List<WorkSet> workSets = workDao.getWork().findAllByTemplate(copyTemp);
            try{
                for(WorkSet wS:workSets){
                    WorkSet work = workDao.getWork().findByTemplate_IdAndExerciseName(baseTemp.getId(), wS.getExerciseName());
                    System.out.println("2nd Try workset exercise is " + work.getExerciseName());
                    List<SubSet> subSets = setDao.getSets().findAllByWorkSet_Id(wS.getId());
                    workDao.getWork().save(work);
                    for(SubSet set: subSets){
                        SubSet newSet = new SubSet();
                        newSet.setWorkSet(work);
                        newSet.setReps(set.getReps());
                        newSet.setExerciseName(set.getExerciseName());
                        newSet.setWeight(set.getWeight());
                        setDao.getSets().save(newSet);
                    }
                    }
                } catch (NullPointerException e) {
                for(WorkSet work : workSets){
                    WorkSet newWork = new WorkSet();
                    newWork.setTemplate(baseTemp);
                    newWork.setExercise(work.getExercise());
                    newWork.setExerciseName(work.getExerciseName());
                    System.out.println("2nd try FAILED newWork exercise is " + work.getExerciseName());
                    workDao.getWork().save(newWork);
                    List<SubSet> subSets = setDao.getSets().findAllByWorkSet_Id(work.getId());
                    for(SubSet set : subSets){
                        SubSet newSet = new SubSet(set);
                        System.out.println(newSet.getExerciseName() + newSet.getReps());

                        newSet.setWorkSet(newWork);
                        setDao.getSets().save(newSet);
                    }
                }
                return "redirect:/exercises/" + id + "/" + day;
                }
                }catch(NullPointerException e){
            template temp = new template();
            temp.setProgram(programDao.getPrograms().findById(id));
            temp.setDay(day);
            System.out.println("1st Try Failed new temp day is " + temp.getDay());
            tempDao.getTemplates().save(temp);
            List<WorkSet> workSets = workDao.getWork().findAllByTemplate(copyTemp);
            for(WorkSet work : workSets){
                WorkSet newWork = new WorkSet(work);
                newWork.setTemplate(temp);
                workDao.getWork().save(newWork);
                List<SubSet> subSets = setDao.getSets().findAllByWorkSet_Id(work.getId());
                for(SubSet set : subSets){
                    SubSet newSet = new SubSet(set);
                    System.out.println(newSet.getExerciseName() + newSet.getReps());

                    newSet.setWorkSet(newWork);
                    setDao.getSets().save(newSet);
                }
            }
            return "redirect:/exercises/" + id + "/" + day;
        }
        return "redirect:/exercises/" + id + "/" + day;

    }
    @PostMapping("/savetemplate/{id}/{day}")
    public String saveTemplate(@PathVariable long id, @PathVariable int day, @RequestParam String tempName){
        template temp = tempDao.getTemplates().findByProgram_IdAndDay(id, day);
        template newTemp = new template();
        newTemp.setDay(day);
        newTemp.setName(tempName);
        newTemp.setUsable(1L);
        tempDao.getTemplates().save(newTemp);
        List<WorkSet> workSets = workDao.getWork().findAllByTemplate(temp);

        for(WorkSet set: workSets){
            List<SubSet> sets = setDao.getSets().findAllByWorkSet_Id(set.getId());
            WorkSet newWork = new WorkSet();
            newWork.setExercise(set.getExercise());
            newWork.setExerciseName(set.getExerciseName());
            newWork.setTemplate(newTemp);
            workDao.getWork().save(newWork);
            for(SubSet subSet: sets){
                SubSet newSet = new SubSet();
                newSet.setWorkSet(newWork);
                newSet.setWeight(subSet.getWeight());
                newSet.setReps(subSet.getReps());
                newSet.setExerciseName(subSet.getExerciseName());
                setDao.getSets().save(newSet);
            }
        }
        return "redirect:/exercises/" + id + "/" + day;
    }

    @RequestMapping(value = "/exercises/{id}", method = RequestMethod.POST)
    public String workoutForDays(@PathVariable Long id, @RequestParam int daychoice) {
        return "redirect:/exercises/" + id + "/" + daychoice;
    }


//    @GetMapping("/exercises/{name}/{day}")
//    public String workoutForDays(@PathVariable String name, @PathVariable int day, Model view){
//        view.addAttribute("day", day);
//        return "/exercises" + name + "/" + day;
//    }

    @GetMapping("/exercises/{progId}/{day}/{id}")
    public String exerciseUpdateIndex(@PathVariable int day, @PathVariable long progId, @PathVariable long id, Model view) {
        Program program = programDao.getPrograms().findById(progId);
        template temp = tempDao.getTemplates().findByProgram_IdAndDay(progId, day);
        List<WorkSet> daySet = workDao.getWork().findAllByTemplate(temp);
        SubSet subSet = new SubSet(0, 0, " ", null);
        long days = program.getProgramDays();

        List<Long> progDays = new ArrayList<>();
        for (long i = 1; i <= days; i++) {
            progDays.add(i);
        }
        view.addAttribute("days", progDays);

        view.addAttribute("dropId", id);
        view.addAttribute("workSets", daySet);
        view.addAttribute("subSet", subSet);
        view.addAttribute("exercises", exerciseService.getExercises().findAll());
        view.addAttribute("day", day);
        view.addAttribute("name", program.getName());
        view.addAttribute("progId", program.getId());
        List<template> templates = tempDao.getTemplates().findAllByUsable(1);

        view.addAttribute("tempList", templates);

        return ("exercises/exercises");
    }

    @GetMapping("/newexercise")
    public String create(Model view) {
        Exercise exercise = new Exercise(" ", " ");
        view.addAttribute("exercise", exercise);
        return ("exercises/newExercise");

    }

    @PostMapping("/newexercise")
    public String createExercise(Exercise exercise) {
        System.out.println(exercise);
        exerciseService.getExercises().save(exercise);
        return "redirect:/exercises";
    }

    @PostMapping("/createplan/{id}/{day}")
    public String createDay(@PathVariable long day, @PathVariable long id, SubSet subSet) {
        System.out.println(subSet.getExerciseName());
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Program program = programDao.getPrograms().findByClient_IdAndId(clientSession.getId(), id);
        SubSet newSub = new SubSet();
        newSub.setWeight(subSet.getWeight());
        newSub.setReps(subSet.getReps());
        newSub.setExerciseName(subSet.getExerciseName());
        try {
            try {
                System.out.println("Program ID is " + program.getId());
                template temp = tempDao.getTemplates().findByProgram_IdAndDay(program.getId(), (int) day);
                System.out.println("Template ID is " + temp.getId());
                WorkSet work = workDao.getWork().findByTemplate_IdAndExerciseName(temp.getId(), subSet.getExerciseName());
                System.out.println(subSet.getExerciseName());
                work.setTemplate(temp);
                System.out.println("Work ID is " + work.getId() + " Temp id is " + temp.getId());
                newSub.setWorkSet(work);
                setDao.getSets().save(newSub);
                long dropid = work.getId();
                return "redirect:/exercises/" + id + "/" + day + "/" + dropid;
            } catch (NullPointerException e) {
                String name = program.getName();
                //Program program = programDao.getPrograms().findByClient_IdAndName(clientSession.getId(), name);
                System.out.println("First try program " + program.getId());
                template temp = tempDao.getTemplates().findByProgram_IdAndDay(program.getId(), (int) day);
                System.out.println("First try temp " + temp.getId());
                WorkSet work = new WorkSet(newSub.getExerciseName(), exerciseService.getExercises().findByName(subSet.getExerciseName()));
                work.setTemplate(temp);
                workDao.getWork().save(work);
                newSub.setWorkSet(work);
                setDao.getSets().save(newSub);
                long dropid = work.getId();
                return "redirect:/exercises/" + id + "/" + day + "/" + dropid;
            }

        } catch (NullPointerException e) {
            template temp = new template((int) day);
            temp.setProgram(program);
            tempDao.getTemplates().save(temp);
            WorkSet work = new WorkSet(newSub.getExerciseName(), exerciseService.getExercises().findByName(subSet.getExerciseName()));
            work.setTemplate(temp);
            workDao.getWork().save(work);
            newSub.setWorkSet(work);
            setDao.getSets().save(newSub);
            long dropid = work.getId();
            return "redirect:/exercises/" + id + "/" + day + "/" + dropid;
        }
    }

    @RequestMapping(value = "/editset/{progId}/{day}", method = RequestMethod.POST)
    public String editSet(@PathVariable long day, @PathVariable long progId, @RequestParam long id, @RequestParam long setId,
                          @RequestParam int weight, @RequestParam int reps) {
        SubSet editSet = setDao.getSets().findOne(setId);
        editSet.setWeight(weight);
        editSet.setReps(reps);
        setDao.getSets().save(editSet);
        return "redirect:/exercises/" + progId + "/" + day + "/" + id;

    }

    @RequestMapping(value = "/copyset/{progId}/{day}", method = RequestMethod.POST)
    public String getTest(@PathVariable long day, @PathVariable long progId, @RequestParam long id, @RequestParam long setId) {
        SubSet copySet = setDao.getSets().findOne(setId);
        SubSet saveSet = new SubSet(copySet.getWeight(), copySet.getReps(), copySet.getExerciseName(), copySet.getWorkSet());
        setDao.getSets().save(saveSet);
        return "redirect:/exercises/" + progId + "/" + day + "/" + id;

    }

    @RequestMapping(value = "/logset/{progId}/{day}", method = RequestMethod.POST)
    public String logSet(@PathVariable long day, @PathVariable long progId, @RequestParam long id, @RequestParam long setId) {
        SubSet subSet = setDao.getSets().findOne(setId);
        Client clientSession = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Exercise exercise = exerciseService.getExercises().findByName(subSet.getExerciseName());
        CompletedSet logSet = new CompletedSet(subSet.getExerciseName(), day, exercise.getId(), subSet.getWeight(), subSet.getReps(), clientSession);
        logSet.setEstimated1RM(subSet.getReps(), subSet.getWeight());
        compDao.getCompSets().save(logSet);
        WorkSet checkWork = subSet.getWorkSet();
        setDao.getSets().delete(setId);
        List<SubSet> subSets = setDao.getSets().findAllByWorkSet_Id(checkWork.getId());
        if (subSets.size() == 0) {
            workDao.getWork().delete(checkWork.getId());
        }
        return "redirect:/log/" + progId + "/" + day + "/" + id;
    }


    @RequestMapping(value = "/deletelogset/{progId}/{day}", method = RequestMethod.POST)
    public String deleteLogSet(@PathVariable long day, @PathVariable long progId, @RequestParam long id, @RequestParam long setId) {
        SubSet checkSet = setDao.getSets().findOne(setId);
        //I am checking the workSet to see if it has an subsets
        //connected to it.  If not I delete the workSet from the database
        System.out.println(id);
        WorkSet checkWork = checkSet.getWorkSet();
        setDao.getSets().delete(checkSet.getId());
        List<SubSet> subSets = setDao.getSets().findAllByWorkSet_Id(checkWork.getId());
        if (subSets.size() == 0) {
            workDao.getWork().delete(checkWork.getId());
        }
        return "redirect:/log/" + progId + "/" + day + "/" + id;
    }



//    @RequestMapping(value = "/logset/{name}/{day}", method = RequestMethod.POST)
//    public String deleteLogset(@PathVariable long day, @PathVariable String name, @RequestParam long id, @RequestParam long setId){
//        SubSet checkSet = setDao.getSets().findOne(setId);
//        //I am checking the workSet to see if it has an subsets
//        //connected to it.  If not I delete the workSet from the database
//        WorkSet checkWork = checkSet.getWorkSet();
//        setDao.getSets().delete(checkSet.getId());
//        List<SubSet> subSets = setDao.getSets().findAllByWorkSet_Id(checkWork.getId());
//        if(subSets.size() == 0){
//            workDao.getWork().delete(checkWork.getId());
//        }
//        return "redirect:/log/" + name + "/" + day ;
//    }
    //

    @RequestMapping(value = "/deleteset/{progId}/{day}", method = RequestMethod.POST)
    public String deleteSet(@PathVariable long day, @PathVariable long progId, @RequestParam long id, @RequestParam long setId) {
        SubSet checkSet = setDao.getSets().findOne(setId);
        //I am checking the workSet to see if it has an subsets
        //connected to it.  If not I delete the workSet from the database
        System.out.println(id);
        WorkSet checkWork = checkSet.getWorkSet();
        setDao.getSets().delete(checkSet.getId());
        List<SubSet> subSets = setDao.getSets().findAllByWorkSet_Id(checkWork.getId());
        if (subSets.size() == 0) {
            workDao.getWork().delete(checkWork.getId());
        }
        return "redirect:/exercises/" + progId + "/" + day + "/" + id;
    }


}