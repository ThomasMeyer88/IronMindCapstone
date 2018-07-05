package com.ironmind.ferrus.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="template")
public class template {
    @Id @GeneratedValue
    private long id;

    @Column(nullable = false)
    private int day;


    @OneToMany(mappedBy = "template")
    private List<WorkSet> WorkSets;

    @ManyToOne
    @JoinColumn
    private Program program;


    public template(){};

    public template(int id, int day) {
        this.id = id;
        this.day = day;

    }

    public template(int day){
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public List<WorkSet> getWorkSets() {
        return WorkSets;
    }

    public void setWorkSets(List<WorkSet> workSets) {
        WorkSets = workSets;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}