package com.ironmind.ferrus.model;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="programs")
public class Program {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @Column
    private long programDays;

    @ManyToOne
    @JoinColumn
    private Client client;

    @OneToMany(mappedBy = "program")
    private List<template> templates;

    public Program(){};

    public Program(String name, Client client) {
        this.name = name;
        this.client = client;
    }

    public Program(String name, Long programDays, Client client){
        this.name = name;
        this.programDays = programDays;
        this.client = client;
    }

    public Program(long id, String Name, Client client){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<template> templates) {
        this.templates = templates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProgramDays() {
        return programDays;
    }

    public void setProgramDays(long programDays) {
        this.programDays = programDays;
    }
}
