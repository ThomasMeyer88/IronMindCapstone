package com.ironmind.ferrus.Services;

import com.ironmind.ferrus.repositiories.Exercises;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseService {
    private Exercises exercises;

    public ExerciseService(){};

    @Autowired
    public ExerciseService(Exercises exercises) {this.exercises = exercises;}

    public Exercises getExercises() {
        return exercises;
    }

    public void setExercises(Exercises exercises) {
        this.exercises = exercises;
    }
}
