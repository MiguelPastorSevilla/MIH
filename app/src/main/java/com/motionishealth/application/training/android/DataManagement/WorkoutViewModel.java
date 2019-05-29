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

    public MutableLiveData<List<Workout>> getWorkoutList() {
        return workoutList;
    }

    public MutableLiveData<Workout> getSelectedWorkout() {
        return selectedWorkout;
    }

    public void setSelectedWorkout(Workout selectedWorkout) {
        this.selectedWorkout.setValue(selectedWorkout);
    }

    public void getWorkoutsFromFirebaseUser(String uid) {
        Log.i(TAG,"Cargando datos de rutinas");

        Exercise ejercicioPrueba1 = new Exercise("Press banca",(long)10,(long)4);
        Exercise ejercicioPrueba2 = new Exercise("Curl biceps",(long)12,(long)5);
        Exercise ejercicioPrueba3 = new Exercise("Squat",(long)8,(long)3);

        List<Exercise> ejerciciosPrueba = new ArrayList<>();
        ejerciciosPrueba.add(ejercicioPrueba1);
        ejerciciosPrueba.add(ejercicioPrueba2);
        ejerciciosPrueba.add(ejercicioPrueba3);

        Workout rutinaPrueba1 = new Workout("Hipertrofia","Ejercicios parar aumentar la capacidad sanguinea",(long)30, (long)8, ejerciciosPrueba);

        List<Workout> rutinasPrueba1 = new ArrayList<>();

        rutinasPrueba1.add(rutinaPrueba1);
        rutinasPrueba1.add(rutinaPrueba1);
        rutinasPrueba1.add(rutinaPrueba1);

        workoutList.setValue(rutinasPrueba1);

        Log.i(TAG,"Datos de rutinas cargados");
    }
}
