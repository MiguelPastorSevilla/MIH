package com.motionishealth.application.training.android.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motionishealth.application.training.android.DataManagement.WorkoutViewModel;
import com.motionishealth.application.training.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEditWorkoutFragment extends Fragment {

    public static final String TAG = "CreateEditWorkoutFragment";
    private WorkoutViewModel workoutViewModel;

    public CreateEditWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_edit_workout, container, false);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        workoutViewModel.getCreateEditWorkout().setValue(null);
    }
}
