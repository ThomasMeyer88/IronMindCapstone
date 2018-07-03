package com.ironmind.ferrus.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="exercises")
public class Exercise {
    @Id @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String muscle;



    public Exercise(){};

    public Exercise(Exercise Copy){
        id = Copy.id;
        name = Copy.name;
        muscle = Copy.muscle;
    }

    public Exercise (String name, String muscle){
        this.name = name;
        this.muscle = muscle;
    }

    public Exercise (long id, String name, String muscle){
        this.id = id;
        this.name = name;
        this.muscle = muscle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }
}

