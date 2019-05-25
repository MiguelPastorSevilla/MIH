package com.motionishealth.application.training.android.POJOs;

public class Exercise {
    private String name;
    private Long reps;
    private Long sets;

    public Exercise() {
    }

    public Exercise(String name, Long reps, Long sets) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getReps() {
        return reps;
    }

    public void setReps(Long reps) {
        this.reps = reps;
    }

    public Long getSets() {
        return sets;
    }

    public void setSets(Long sets) {
        this.sets = sets;
    }
}
