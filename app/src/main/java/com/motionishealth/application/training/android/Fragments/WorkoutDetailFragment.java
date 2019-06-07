package com.motionishealth.application.training.android.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.motionishealth.application.training.android.DataManagement.WorkoutViewModel;
import com.motionishealth.application.training.android.POJOs.Exercise;
import com.motionishealth.application.training.android.POJOs.Workout;
import com.motionishealth.application.training.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutDetailFragment extends Fragment {

    //TAG
    public static final String WORKOUT_DETAIL_FRAGMENT_TAG = "WorkoutDetailFragment";

    //Modelo de la aplicación
    private WorkoutViewModel workoutViewModel;
    //Rutina que mostraremos
    private Workout workout;

    public WorkoutDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Recuperamos la referencia al modelo.
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
        //Recuperamos la rutina seleccionada.
        workout = workoutViewModel.getSelectedWorkout().getValue();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Recuperamos la vista a inflar.
        View v = inflater.inflate(R.layout.fragment_workout_detail, container, false);

        //Recuperamos los elementos de la vista.
        LinearLayout lyFragmentWorkoutDetails = v.findViewById(R.id.lyFragmentWorkoutDetails);

        TextView tvWorkoutDetailsName = v.findViewById(R.id.tvItemWorkoutDetailName);
        TextView tvWorkoutDetailsDescription = v.findViewById(R.id.tvItemWorkoutDetailDescription);
        TextView tvWorkoutDetailsET = v.findViewById(R.id.tvItemWorkoutDetailET);

        //Asignamos el texto de los elementos en función de la rutina seleccionada.
        tvWorkoutDetailsName.setText("Nombre: " + workout.getName());
        tvWorkoutDetailsDescription.setText("Descripción: " + workout.getDescription());
        tvWorkoutDetailsET.setText("Tiempo estimado de duración: " + workout.getEstimatedTimeInMinutes().toString());

        //Cargamos todos los ejercicios de la lista de ejercicios de la rutina seleccionada en una vista, la
        //cual añadimos a la vista a devolver.
        for (Exercise e : workout.getExercises()) {
            //Vista a añadir.
            View exerciseView = inflater.inflate(R.layout.item_workout_exercise_details, lyFragmentWorkoutDetails, false);

            //Recuperacion de los elementos de la vista.
            TextView tvExerciseName = exerciseView.findViewById(R.id.tvItemExerciseName);
            TextView tvExerciseReps = exerciseView.findViewById(R.id.tvItemExerciseReps);
            TextView tvExerciseSets = exerciseView.findViewById(R.id.tvItemExerciseSets);

            //Asignación del valor a los elementos de la interfaz.
            tvExerciseName.setText(e.getName());
            tvExerciseReps.setText(e.getReps().toString());
            tvExerciseSets.setText(e.getSets().toString());

            //Añadido de la vista con los datos del ejercicio a la vista general.
            lyFragmentWorkoutDetails.addView(exerciseView);
        }

        return v;
    }

}
