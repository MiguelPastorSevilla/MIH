package com.motionishealth.application.training.android.DataManagement;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.motionishealth.application.training.android.POJOs.Exercise;
import com.motionishealth.application.training.android.POJOs.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutViewModel extends ViewModel {

    private final static String TAG = "ViewModel";

    private MutableLiveData<List<Workout>> workoutList = new MutableLiveData<>();
    private MutableLiveData<Workout> selectedWorkout = new MutableLiveData<>();
    private MutableLiveData<Workout> createEditWorkout = new MutableLiveData<>();
    private MutableLiveData<Boolean> creatingEditingWorkout = new MutableLiveData<>();
    private MutableLiveData<Boolean> workoutListChanged = new MutableLiveData<>();

    public MutableLiveData<Boolean> getCreatingEditingWorkout() {
        return creatingEditingWorkout;
    }

    public MutableLiveData<Boolean> getWorkoutListChanged() {
        return workoutListChanged;
    }

    public MutableLiveData<List<Workout>> getWorkoutList() {
        return workoutList;
    }

    public MutableLiveData<Workout> getSelectedWorkout() {
        return selectedWorkout;
    }

    public void setSelectedWorkout(Workout selectedWorkout) {
        this.selectedWorkout.setValue(selectedWorkout);
    }

    public MutableLiveData<Workout> getCreateEditWorkout() {
        return createEditWorkout;
    }

    public void getWorkoutsFromFirebaseUser(String uid) {
        Log.i(TAG,"Cargando datos de rutinas");
        if (workoutListChanged.getValue()==null){
            //Cargar datos de firebase
            workoutList.setValue(new ArrayList<Workout>());
        }
        workoutListChanged.setValue(false);
        Log.i(TAG,"Datos de rutinas cargados");
    }

    public void addWorkoutToList(Workout workout){
        List<Workout> workouts = workoutList.getValue();
        workouts.add(workout);
        workoutList.setValue(workouts);

        //Subir datos a firebase
    }
}
