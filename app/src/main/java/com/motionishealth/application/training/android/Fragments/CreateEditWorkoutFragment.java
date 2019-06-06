package com.motionishealth.application.training.android.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.motionishealth.application.training.android.Adapter.ExerciseCreationAdapter;
import com.motionishealth.application.training.android.DataManagement.WorkoutViewModel;
import com.motionishealth.application.training.android.POJOs.Exercise;
import com.motionishealth.application.training.android.POJOs.Workout;
import com.motionishealth.application.training.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEditWorkoutFragment extends Fragment {

    public static final String TAG = "CreateEditWorkoutFrag";
    private WorkoutViewModel workoutViewModel;

    private EditText etItemWorkoutNameCreateEdit;
    private EditText etItemWorkoutDescriptionCreateEdit;
    private EditText etItemWorkoutETCreateEdit;

    private RadioGroup rgItemWorkoutDifficulty;

    private ListView lvExerciseList;

    private TextView tvItemExerciseNameHeaderCreateEdit;
    private TextView tvItemExerciseRepsHeaderCreateEdit;
    private TextView tvItemExerciseSetsHeaderCreateEdit;

    private EditText etItemExerciseNameCreateEdit;
    private EditText etItemExerciseRepsCreateEdit;
    private EditText etItemExerciseSetsCreateEdit;
    private ImageButton btItemExerciseAddExercise;

    private FloatingActionButton fabSaveWorkout;

    private ExerciseCreationAdapter exerciseCreationAdapter;

    public CreateEditWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
        exerciseCreationAdapter = new ExerciseCreationAdapter(getContext(), new ArrayList<Exercise>());
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_edit_workout, container, false);

        etItemWorkoutNameCreateEdit = v.findViewById(R.id.etItemWorkoutNameCreateEdit);
        etItemWorkoutDescriptionCreateEdit = v.findViewById(R.id.etItemWorkoutDescriptionCreateEdit);
        etItemWorkoutETCreateEdit = v.findViewById(R.id.etItemWorkoutETCreateEdit);
        rgItemWorkoutDifficulty = v.findViewById(R.id.rgItemWorkoutDifficulty);

        lvExerciseList = v.findViewById(R.id.lvExerciseCreationList);

        tvItemExerciseNameHeaderCreateEdit = v.findViewById(R.id.tvItemExerciseNameHeaderCreateEdit);
        tvItemExerciseRepsHeaderCreateEdit = v.findViewById(R.id.tvItemExerciseRepsHeaderCreateEdit);
        tvItemExerciseSetsHeaderCreateEdit = v.findViewById(R.id.tvItemExerciseSetsHeaderCreateEdit);

        tvItemExerciseNameHeaderCreateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etItemExerciseNameCreateEdit.requestFocus();
            }
        });

        tvItemExerciseRepsHeaderCreateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etItemExerciseRepsCreateEdit.requestFocus();
            }
        });

        tvItemExerciseSetsHeaderCreateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etItemExerciseSetsCreateEdit.requestFocus();
            }
        });

        etItemExerciseNameCreateEdit = v.findViewById(R.id.etItemExerciseNameCreateEdit);
        etItemExerciseRepsCreateEdit = v.findViewById(R.id.etItemExerciseRepsCreateEdit);
        etItemExerciseSetsCreateEdit = v.findViewById(R.id.etItemExerciseSetsCreateEdit);

        fabSaveWorkout = v.findViewById(R.id.fabSaveWorkout);

        etItemExerciseSetsCreateEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btItemExerciseAddExercise.callOnClick();
                }
                return false;
            }
        });

        btItemExerciseAddExercise = v.findViewById(R.id.btItemExerciseAddExercise);

        btItemExerciseAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exerciseName = etItemExerciseNameCreateEdit.getText().toString();
                Long exerciseReps = (long) 0;
                Long exerciseSets = (long) 0;
                try {
                    exerciseReps = Long.parseLong(etItemExerciseRepsCreateEdit.getText().toString());
                    exerciseSets = Long.parseLong(etItemExerciseSetsCreateEdit.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_exercises_too_long), Toast.LENGTH_SHORT
                    ).show();
                    Log.e(TAG, e.getMessage());
                    return;
                }
                if (!exerciseName.trim().isEmpty()) {
                    Exercise exercise = new Exercise(exerciseName, exerciseReps, exerciseSets);
                    exerciseCreationAdapter.getExercises().add(exercise);
                    exerciseCreationAdapter.notifyDataSetChanged();
                    etItemExerciseNameCreateEdit.setText("");
                    etItemExerciseRepsCreateEdit.setText("");
                    etItemExerciseSetsCreateEdit.setText("");
                    etItemExerciseNameCreateEdit.requestFocus();
                }else{
                    Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_exercises_no_name), Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        fabSaveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySaveWorkout();
            }
        });

        Workout workoutReceived = workoutViewModel.getCreateEditWorkout().getValue();

        try {
            if (workoutReceived.getName() == null) {
                workoutViewModel.getCreatingWorkout().setValue(true);
            } else {
                workoutViewModel.getEditingWorkout().setValue(true);
            }
        } catch (NullPointerException npe) {
            workoutViewModel.getCreatingWorkout().setValue(true);
        }

        if (workoutViewModel.getEditingWorkout().getValue()) {
            etItemWorkoutNameCreateEdit.setText(workoutReceived.getName());
            etItemWorkoutDescriptionCreateEdit.setText(workoutReceived.getDescription());
            etItemWorkoutETCreateEdit.setText(workoutReceived.getEstimatedTimeInMinutes().toString());
            if (workoutReceived.getDifficulty() == (long) 0) {
                rgItemWorkoutDifficulty.check(R.id.rbItemWorkoutDifficultyEasy);
            } else if (workoutReceived.getDifficulty() == (long) 1) {
                rgItemWorkoutDifficulty.check(R.id.rbItemWorkoutDifficultyMedium);
            } else {
                rgItemWorkoutDifficulty.check(R.id.rbItemWorkoutDifficultyHard);
            }
            exerciseCreationAdapter = new ExerciseCreationAdapter(getContext(), (ArrayList<Exercise>) workoutReceived.getExercises());
        }

        lvExerciseList.setAdapter(exerciseCreationAdapter);

        return v;
    }

    private void trySaveWorkout() {
        Log.i(TAG, "Intento de guardar rutina");
        if (etItemWorkoutNameCreateEdit.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_name), Toast.LENGTH_SHORT
            ).show();
            Log.w(TAG, "Nombre de rutina vacío");
            return;
        }
        if (etItemWorkoutDescriptionCreateEdit.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_description), Toast.LENGTH_SHORT
            ).show();
            Log.w(TAG, "Descripción de rutina vacía ");
            return;
        }
        if (etItemWorkoutETCreateEdit.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_et), Toast.LENGTH_SHORT
            ).show();
            Log.w(TAG, "Tiempo estimado de rutina vacío");
            return;
        }
        try{
            if (Long.parseLong(etItemWorkoutETCreateEdit.getText().toString()) <= 0) {
                Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_et_zero), Toast.LENGTH_SHORT
                ).show();
                Log.w(TAG, "Tiempo estimado de rutina es 0");
                return;
            }
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_et_too_long), Toast.LENGTH_SHORT
            ).show();
            return;
        }
        Long workoutDifficulty;
        switch (rgItemWorkoutDifficulty.getCheckedRadioButtonId()) {
            case R.id.rbItemWorkoutDifficultyEasy:
                workoutDifficulty = (long) 0;
                break;
            case R.id.rbItemWorkoutDifficultyMedium:
                workoutDifficulty = (long) 1;
                break;
            case R.id.rbItemWorkoutDifficultyHard:
                workoutDifficulty = (long) 2;
                break;
            default:
                Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_difficulty), Toast.LENGTH_SHORT
                ).show();
                Log.w(TAG, "Ninguna dificultad seleccionada");
                return;
        }

        if (exerciseCreationAdapter.getExercises().size() <= 0) {
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_exercises), Toast.LENGTH_SHORT
            ).show();
            Log.w(TAG, "Ningun ejercicio creado");
            return;
        }

        Workout workout;

        if (workoutViewModel.getCreatingWorkout().getValue()) {
            workout = new Workout(etItemWorkoutNameCreateEdit.getText().toString(),
                    etItemWorkoutDescriptionCreateEdit.getText().toString(),
                    Long.parseLong(etItemWorkoutETCreateEdit.getText().toString()),
                    workoutDifficulty,
                    exerciseCreationAdapter.getExercises());
        } else {
            workout = workoutViewModel.getCreateEditWorkout().getValue();
            workout.setName(etItemWorkoutNameCreateEdit.getText().toString());
            workout.setDescription(etItemWorkoutDescriptionCreateEdit.getText().toString());
            workout.setEstimatedTimeInMinutes(Long.parseLong(etItemWorkoutETCreateEdit.getText().toString()));
            workout.setDifficulty(workoutDifficulty);
            workout.setExercises(exerciseCreationAdapter.getExercises());
        }

        workoutViewModel.addWorkoutToList(workout);
        workoutViewModel.getWorkoutListChanged().setValue(true);
        workoutViewModel.getCreateEditWorkout().setValue(null);
        workoutViewModel.getEditingWorkout().setValue(false);
        workoutViewModel.getCreatingWorkout().setValue(false);
    }
}
