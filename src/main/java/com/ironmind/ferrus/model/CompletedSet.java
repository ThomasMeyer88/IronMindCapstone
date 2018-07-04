package com.ironmind.ferrus.model;

import javax.persistence.*;

@Entity
@Table(name="completedsets")
public class CompletedSet {
    @Id @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String exerciseName;

    @Column(nullable = false)
    private long day;

    @Column(nullable = false)
    private long exerciseId;

    @Column(nullable = false)
    private long weight;

    @Column(nullable = false)
    private long reps;

    @Column(nullable = false)
    private long totalweight;

    @Column(nullable = false)
    private long estimated1RM;

    @ManyToOne
    @JoinColumn
    private Client client;

    public CompletedSet(){};

    public CompletedSet(String exerciseName, long day, long exerciseId, long weight, long reps, Client client) {
        this.exerciseName = exerciseName;
        this.day = day;
        this.exerciseId = exerciseId;
        this.weight = weight;
        this.reps = reps;
        this.totalweight = (weight * reps);
        this.estimated1RM = (weight * (1 + (reps/30)));
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public long getReps() {
        return reps;
    }

    public void setReps(long reps) {
        this.reps = reps;
    }

    public long getTotalweight() {
        return totalweight;
    }

    public void setTotalweight(long totalweight) {
        this.totalweight = totalweight;
    }

    public long getEstimated1RM() {
        return estimated1RM;
    }

    public void setEstimated1RM(long estimated1RM) {
        this.estimated1RM = estimated1RM;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
