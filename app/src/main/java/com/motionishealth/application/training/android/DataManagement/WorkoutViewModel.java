package com.motionishealth.application.training.android.DataManagement;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motionishealth.application.training.android.POJOs.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutViewModel extends ViewModel {

    private final static String TAG = "ViewModel";

    private DatabaseReference database;
    private String userUID;

    private MutableLiveData<List<Workout>> workoutList = new MutableLiveData<>();
    private MutableLiveData<Workout> selectedWorkout = new MutableLiveData<>();
    private MutableLiveData<Workout> createEditWorkout = new MutableLiveData<>();
    private MutableLiveData<Boolean> workoutListChanged = new MutableLiveData<>();
    private MutableLiveData<Boolean> noWorkoutsAvailable = new MutableLiveData<>();
    private MutableLiveData<Boolean> editingWorkout = new MutableLiveData<>();
    private MutableLiveData<Boolean> creatingWorkout = new MutableLiveData<>();


    public MutableLiveData<Boolean> getCreatingWorkout() {
        return creatingWorkout;
    }

    public void setCreatingWorkout(MutableLiveData<Boolean> creatingWorkout) {
        this.creatingWorkout = creatingWorkout;
    }


    public MutableLiveData<Boolean> getEditingWorkout() {
        return editingWorkout;
    }

    public void setEditingWorkout(MutableLiveData<Boolean> editingWorkout) {
        this.editingWorkout = editingWorkout;
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

    public MutableLiveData<Boolean> getNoWorkoutsAvailable() {
        return noWorkoutsAvailable;
    }

    public void getWorkoutsFromFirebaseUser(String uid) {
        userUID = uid;
        Log.i(TAG, "Cargando datos de rutinas");
        if (workoutListChanged.getValue() == null) {
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseContract.USERS_NODE)
                    .child(userUID)
                    .child(FirebaseContract.USER_WORKOUTS).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Workout> workoutsFromFirebase = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Workout workout = snapshot.getValue(Workout.class);
                        workout.setKey(snapshot.getKey());
                        workoutsFromFirebase.add(workout);
                    }
                    workoutList.setValue(workoutsFromFirebase);
                    if (workoutsFromFirebase.size() == 0) {
                        noWorkoutsAvailable.setValue(true);
                    } else {
                        noWorkoutsAvailable.setValue(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    noWorkoutsAvailable.setValue(true);
                }
            });
            workoutList.setValue(new ArrayList<Workout>());
        }
        workoutListChanged.setValue(false);
        Log.i(TAG, "Datos de rutinas cargados");
    }

    public void addWorkoutToList(Workout workout) {
        if (creatingWorkout.getValue()) {
            List<Workout> workouts = workoutList.getValue();
            database = FirebaseDatabase.getInstance().getReference().child(FirebaseContract.USERS_NODE).child(userUID).child(FirebaseContract.USER_WORKOUTS);
            String key = database.push().getKey();
            workout.setKey(key);
            workouts.add(workout);
            workoutList.setValue(workouts);
            database.child(key).setValue(workout);
            noWorkoutsAvailable.setValue(false);
        } else {
            List<Workout> workouts = workoutList.getValue();
            database = FirebaseDatabase.getInstance().getReference().child(FirebaseContract.USERS_NODE).child(userUID).child(FirebaseContract.USER_WORKOUTS).child(workout.getKey());
            database.setValue(workout);
            workouts.remove(workout);
            workouts.add(workout);
            workoutList.setValue(workouts);
            noWorkoutsAvailable.setValue(false);
        }

    }

    public void removeWorkoutFromList(Workout workout) {
        List<Workout> workouts = workoutList.getValue();
        FirebaseDatabase.getInstance().getReference().child(FirebaseContract.USERS_NODE).child(userUID).child(FirebaseContract.USER_WORKOUTS)
                .child(workout.getKey()).setValue(null);

        workouts.remove(workout);
        workoutList.setValue(workouts);
    }
}
