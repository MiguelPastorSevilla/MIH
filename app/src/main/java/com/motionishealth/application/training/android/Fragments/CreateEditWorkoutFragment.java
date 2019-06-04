package com.motionishealth.application.training.android.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.motionishealth.application.training.android.Adapter.ExerciseCreationAdapter;
import com.motionishealth.application.training.android.DataManagement.WorkoutViewModel;
import com.motionishealth.application.training.android.POJOs.Exercise;
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

    private EditText etItemExerciseNameCreateEdit;
    private EditText etItemExerciseRepsCreateEdit;
    private EditText etItemExerciseSetsCreateEdit;
    private ImageButton btItemExerciseAddExercise;

    private ExerciseCreationAdapter exerciseCreationAdapter;

    public CreateEditWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
        exerciseCreationAdapter = new ExerciseCreationAdapter(getContext());
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
        lvExerciseList.setAdapter(exerciseCreationAdapter);

        etItemExerciseNameCreateEdit = v.findViewById(R.id.etItemExerciseNameCreateEdit);
        etItemExerciseRepsCreateEdit = v.findViewById(R.id.etItemExerciseRepsCreateEdit);
        etItemExerciseSetsCreateEdit = v.findViewById(R.id.etItemExerciseSetsCreateEdit);


        etItemExerciseSetsCreateEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
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
                Long exerciseReps = (long)0;
                Long exerciseSets = (long)0;
                try {
                    exerciseReps = Long.parseLong(etItemExerciseRepsCreateEdit.getText().toString());
                    exerciseSets = Long.parseLong(etItemExerciseSetsCreateEdit.getText().toString());
                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                }
                if (!exerciseName.isEmpty()){
                    Exercise exercise = new Exercise(exerciseName,exerciseReps,exerciseSets);
                    exerciseCreationAdapter.getExercises().add(exercise);
                    exerciseCreationAdapter.notifyDataSetChanged();
                    etItemExerciseNameCreateEdit.setText("");
                    etItemExerciseRepsCreateEdit.setText("");
                    etItemExerciseSetsCreateEdit.setText("");
                    etItemExerciseNameCreateEdit.requestFocus();
                }
            }
        });
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        workoutViewModel.getCreateEditWorkout().setValue(null);
    }
}
