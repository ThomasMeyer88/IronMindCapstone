package com.ironmind.ferrus.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="WorkSet")
public class WorkSet {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private int setNo;

    @Column(nullable = false)
    private String exerciseName;


    @ManyToOne
    @JoinColumn
    private Exercise exercise;

    @ManyToOne
    @JoinColumn
    private template template;

    @OneToMany(mappedBy = "workSet")
    private List<SubSet> subSets;

    public WorkSet(){};

    public WorkSet(String exerciseName, Exercise exercise) {
        this.exerciseName = exerciseName;
        this.exercise = exercise;
    }

    public WorkSet(String exerciseName){
        this.exerciseName = exerciseName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSetNo() {
        return setNo;
    }

    public void setSetNo(int setNo) {
        this.setNo = setNo;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }


    public template getTemplate() {
        return template;
    }

    public void setTemplate(template template) {
        this.template = template;
    }

    public List<SubSet> getSubSets() {
        return subSets;
    }

    public void setSubSets(List<SubSet> subSets) {
        this.subSets = subSets;
    }
}
