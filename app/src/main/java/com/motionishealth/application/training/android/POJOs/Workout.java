package com.motionishealth.application.training.android.POJOs;

import java.util.List;

public class Workout {
    private String name;
    private String description;
    private Long estimatedTimeInMinutes;
    private Long difficulty;
    private List<Exercise> exercises;
    public Workout() {
    }

    public Workout(String name, String description, Long estimatedTimeInMinutes, Long difficulty, List<Exercise> exercises) {
        this.name = name;
        this.description = description;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.difficulty = difficulty;
        this.exercises = exercises;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(Long estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public Long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Long difficulty) {
        this.difficulty = difficulty;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }


}
