package com.motionishealth.application.training.android.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

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

    private static final String TAG = "WorkoutFragment";
    public static final String WORKOUT_FRAGMENT_TAG = "WorkoutFragment";

    private ListView lvWorkoutList;
    private WorkoutViewModel workoutViewModel;
    private ProgressBar pbLoadingMainList;
    private FirebaseUser user;


    public WorkoutListFragment() {
        // Required empty public constructor
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        workoutViewModel.getWorkoutsFromFirebaseUser(user.getUid());
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout_list, container, false);
        lvWorkoutList = v.findViewById(R.id.lvWorkoutList);
        pbLoadingMainList = v.findViewById(R.id.pbLoadingMainList);
        workoutViewModel.getWorkoutList().observe(getActivity(), new Observer<List<Workout>>() {
            @Override
            public void onChanged(@Nullable List<Workout> workouts) {
                pbLoadingMainList.setVisibility(View.GONE);
                WorkoutAdapter adapter = new WorkoutAdapter(getContext(), workoutViewModel.getWorkoutList().getValue());
                lvWorkoutList.setAdapter(adapter);
                Log.i(TAG, "Lista actualizada, progress bar quitada.");
            }
        });


        return v;
    }

}
