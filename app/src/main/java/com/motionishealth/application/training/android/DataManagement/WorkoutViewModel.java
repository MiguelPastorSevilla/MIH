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

    //Tag
    private final static String TAG = "ViewModel";

    //Referencia a la base de datos de Firebase.
    private DatabaseReference database;
    //ID de usuario
    private String userUID;

    //Datos del modelo.
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

    /**
     * Método para recuperar una lista de rutinas del usuario de firebase cuyo ID es pasado.
     * @param uid Id del usuario.
     */
    public void getWorkoutsFromFirebaseUser(String uid) {
        //Asignamos el uid del usuario.
        userUID = uid;
        Log.i(TAG, "Cargando datos de rutinas");
        //Esta comprobación hace que se recuperen los datos de la base de datos únicamente cuando
        //se inicia la aplicación.
        if (workoutListChanged.getValue() == null) {
            //Recuperación de datos de la rutina "Usuarios -> ID de usuario -> Rutinas"
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseContract.USERS_NODE)
                    .child(userUID)
                    .child(FirebaseContract.USER_WORKOUTS).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Lista con las rutinas recuperadas.
                    ArrayList<Workout> workoutsFromFirebase = new ArrayList<>();
                    //Recorremos todas las rutinas de la ruta de la base de datos y añadimos todas las rutinas
                    //a la lista.
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Workout workout = snapshot.getValue(Workout.class);
                        workout.setKey(snapshot.getKey());
                        workoutsFromFirebase.add(workout);
                    }
                    //Asignamos a la variable del modelo las rutinas recuperadas.
                    workoutList.setValue(workoutsFromFirebase);
                    //Asignamos a la variable de modelo "Sin rutinas disponibles" segun si hay rutinas o no.
                    if (workoutsFromFirebase.size() == 0) {
                        noWorkoutsAvailable.setValue(true);
                    } else {
                        noWorkoutsAvailable.setValue(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Asignamos a la variable de modelo "Sin rutinas disponibles" hay rutinas algún error.
                    noWorkoutsAvailable.setValue(true);
                }
            });
            //Como la recuperación de datos se ejecuta de forma asíncrona, asignamos una lista vacía para evitar.
            //excepciones de tipo "nullPointerException".
            workoutList.setValue(new ArrayList<Workout>());
        }
        ///Asignamos a la variable de modelo "Lista Cambiada".
        workoutListChanged.setValue(false);
        Log.i(TAG, "Datos de rutinas cargados");
    }

    /**
     * Método para añadir o actualizar una rutina en la base de datos.
     * @param workout Rutina a añadir o actualizar.
     */
    public void addWorkoutToList(Workout workout) {
        //Si estamos creando una rutina, añadiremos una rutina nueva a la base de datos.
        if (creatingWorkout.getValue()) {
            //Recuperamos la lista de rutinas.
            List<Workout> workouts = workoutList.getValue();
            //Recuperamos la referencia a la ruta "Usuarios -> ID de usuario -> Rutinas" en la base de datos.
            database = FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseContract.USERS_NODE)
                    .child(userUID)
                    .child(FirebaseContract.USER_WORKOUTS);
            //Generamos una entrada nueva y guardamos la clave.
            String key = database.push().getKey();
            //Le asignamos la clave de la ruta donde estará almacenada al objeto Rtuina.
            workout.setKey(key);
            //Añadimos la rutina a la lista de rutinas.
            workouts.add(workout);
            //Asignamos la lista actualizada a la variable del modelo.
            workoutList.setValue(workouts);
            //Asignamos la rutina a la ruta generada con el método push() en la base de datos.
            database.child(key).setValue(workout);
            //Como ya tendremos como mínimo 1 rutina, cambiamos el valor de "Sin rutinas disponibles".
            noWorkoutsAvailable.setValue(false);
        } else {
            //Llegamos a este caso si estamos modificando una rutina.
            //Recuperamos la lista de rutinas.
            List<Workout> workouts = workoutList.getValue();
            //Recuperamos la ruta de la base de datos dónde se encuentra guardada la rutina.
            database = FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseContract.USERS_NODE)
                    .child(userUID)
                    .child(FirebaseContract.USER_WORKOUTS)
                    .child(workout.getKey());
            //Actualizamos el valor de la rutina.
            database.setValue(workout);
            //Borramos la rutina de la lista y la volvemos a añadir actualizada.
            workouts.remove(workout);
            workouts.add(workout);
            //Actualizamos la lista de rutinas.
            workoutList.setValue(workouts);
            //Como ya tendremos como mínimo 1 rutina, cambiamos el valor de "Sin rutinas disponibles".
            noWorkoutsAvailable.setValue(false);
        }

    }

    /**
     * Método para borrar una rutina de la lista.
     * @param workout
     */
    public void removeWorkoutFromList(Workout workout) {
        List<Workout> workouts = workoutList.getValue();
        //Para borrar una rutina de la base de datos, le asignamos valor nulo. Así funciona firebase.
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseContract.USERS_NODE)
                .child(userUID)
                .child(FirebaseContract.USER_WORKOUTS)
                .child(workout.getKey()).setValue(null);
        workouts.remove(workout);
        workoutList.setValue(workouts);
    }
}
