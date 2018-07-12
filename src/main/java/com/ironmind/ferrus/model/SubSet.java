package com.ironmind.ferrus.model;

import javax.persistence.*;

@Entity
@Table(name="SubSet")
public class SubSet {
    @Id
    @GeneratedValue
    private long id;

    @Column (nullable=false)
    private int weight;

    @Column (nullable = false)
    private int reps;

    @Column (nullable=false)
    private String exerciseName;

    @ManyToOne
    @JoinColumn
    private WorkSet workSet;


    public SubSet(){};

    public SubSet(long id, int weight, int reps, WorkSet WorkSet){
        this.id = id;
        this.weight = weight;
        this.reps = reps;
        this.workSet = WorkSet;
    }
    public SubSet(int weight, int reps, String exerciseName, WorkSet WorkSet) {
        this.weight = weight;
        this.reps = reps;
        this.exerciseName = exerciseName;
        this.workSet = WorkSet;
    }

    public SubSet(SubSet subSet){
        this.weight = subSet.weight;
        this.reps = subSet.reps;
        this.exerciseName = subSet.exerciseName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public WorkSet getWorkSet() {
        return workSet;
    }

    public void setWorkSet(WorkSet WorkSet) {
        this.workSet = WorkSet;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

