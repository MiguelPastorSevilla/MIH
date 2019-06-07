package com.motionishealth.application.training.android.POJOs;

import java.util.List;
import java.util.Objects;

public class Workout {
    //Clave de la rutina
    private String key;
    //Nombre de la rutina
    private String name;
    //Descripción de la rutina
    private String description;
    //Tiempo estimado de duración de la rutina
    private Long estimatedTimeInMinutes;
    //Dificultad de la rutina
    private Long difficulty;
    //Lista de ejercicios de la rutina
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //Comparamos la rutina por la clave que tenga únicamente.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workout workout = (Workout) o;
        return Objects.equals(key, workout.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
