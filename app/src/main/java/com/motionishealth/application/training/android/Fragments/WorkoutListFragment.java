package com.motionishealth.application.training.android.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.motionishealth.application.training.android.Adapter.WorkoutAdapter;
import com.motionishealth.application.training.android.DataManagement.WorkoutViewModel;
import com.motionishealth.application.training.android.POJOs.Workout;
import com.motionishealth.application.training.android.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutListFragment extends Fragment {

    //Log tag
    private static final String LOG_TAG = "WorkoutFragment";
    //Tag del fragment
    public static final String WORKOUT_FRAGMENT_TAG = "WorkoutFragment";

    //Elementos de interfaz
    private ListView lvWorkoutList;
    private ProgressBar pbLoadingMainList;
    private FloatingActionButton fbAddWorkout;

    //Modelo de la apliiación
    private WorkoutViewModel workoutViewModel;

    //Usuario de la aplicación
    private FirebaseUser user;

    public WorkoutListFragment() {
        // Required empty public constructor
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //Recuperación de la referencia del modelo
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
        //Carga de la lista de rutinas de la base de datos
        workoutViewModel.getWorkoutsFromFirebaseUser(user.getUid());
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Vista de la interfaz del fragment
        View v = inflater.inflate(R.layout.fragment_workout_list, container, false);

        //Recuperación elementos de interfaz
        lvWorkoutList = v.findViewById(R.id.lvWorkoutList);
        pbLoadingMainList = v.findViewById(R.id.pbLoadingMainList);
        fbAddWorkout = v.findViewById(R.id.fbAddWorkout);

        //Listener de clic corto en una rutina
        lvWorkoutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                //Recuperamos la rutina seleccionada
                Workout selected = (Workout) lvWorkoutList.getAdapter().getItem(pos);
                //Actualizamos la rutina seleccionada del modelo
                workoutViewModel.setSelectedWorkout(selected);
            }
        });
        //Listener de clic largo en una rutina
        lvWorkoutList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                //Recuperamos la rutina seleccionada
                Workout selected = (Workout) lvWorkoutList.getAdapter().getItem(pos);
                //Actualizamos el valor de la rutina a crear o editar del modelo
                workoutViewModel.getCreateEditWorkout().setValue(selected);
                return true;
            }
        });

        //Observer de la lista de rutinas del modelo.
        workoutViewModel.getWorkoutList().observe(getActivity(), new Observer<List<Workout>>() {
            @Override
            public void onChanged(@Nullable List<Workout> workouts) {
                //Comprobación de lista deja de ser nula y tiene al menos un ejercicio
                if (workouts != null && workouts.size() > 0) {
                    //Quitamos la barra de progreso
                    pbLoadingMainList.setVisibility(View.GONE);
                    //Cargamos un nuevo adaptador con los ejercicios.
                    WorkoutAdapter adapter = new WorkoutAdapter(getContext(), workouts);
                    adapter.setWorkoutViewModel(workoutViewModel);
                    lvWorkoutList.setAdapter(adapter);
                    Log.i(LOG_TAG, "Lista actualizada, progress bar quitada.");
                }
            }
        });

        //Observer de la variable del modelo que indica que no hay rutinas disponibles.
        workoutViewModel.getNoWorkoutsAvailable().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                //Comprobación de que no hay rutinas disponibles.
                if (aBoolean != null && aBoolean) {
                    //Si no hay rutinas disponibles, quitamos la barra de progreso y mostramos un mensaje.
                    pbLoadingMainList.setVisibility(View.GONE);
                    if (WorkoutListFragment.this.isAdded()){
                        Toast.makeText(getContext(), getResources().getString(R.string.fragments_workout_list_noWorkouts), Toast.LENGTH_LONG).show();
                    }
                    Log.i(LOG_TAG, "No hay rutinas, progress bar quitada.");
                }
            }
        });
        //Listener del botón de añadir nueva rutina.
        fbAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Añadimos una rutina vacía a la variable del modelo que contiene la rutina a crear o editar.
                workoutViewModel.getCreateEditWorkout().setValue(new Workout());
            }
        });
        return v;
    }

}
