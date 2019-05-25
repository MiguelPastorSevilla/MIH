package com.motionishealth.application.training.android.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.motionishealth.application.training.android.Adapter.WorkoutAdapter;
import com.motionishealth.application.training.android.POJOs.Workout;
import com.motionishealth.application.training.android.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutListFragment extends Fragment {

    private List<Workout> workouts;
    private ListView lvWorkoutList;

    public WorkoutListFragment() {
        // Required empty public constructor
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout_list, container, false);
        lvWorkoutList = v.findViewById(R.id.lvWorkoutList);
        WorkoutAdapter adapter = new WorkoutAdapter(getContext(),this.workouts);
        lvWorkoutList.setAdapter(adapter);
        return v;
    }

}
