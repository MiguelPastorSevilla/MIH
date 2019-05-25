package com.motionishealth.application.training.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.motionishealth.application.training.android.POJOs.Workout;
import com.motionishealth.application.training.android.R;

import org.w3c.dom.Text;

import java.util.List;

public class WorkoutAdapter extends BaseAdapter {

    private List<Workout> workouts;
    private Context context;

    public WorkoutAdapter(Context context, List<Workout> workouts) {
        this.context = context;
        this.workouts = workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return workouts.size();
    }

    @Override
    public Object getItem(int i) {
        return workouts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(this.context).inflate(R.layout.item_workout_master,viewGroup,false);
        }

        Workout currentWorkout = (Workout)getItem(i);

        TextView tvName = view.findViewById(R.id.tvItemWorkoutMasterName);
        TextView tvET = view.findViewById(R.id.tvItemWorkoutMasterET);
        TextView tvExerciseCount = view.findViewById(R.id.tvItemWorkoutMasterExerciseNumber);

        tvName.setText("Nombre: "+currentWorkout.getName());
        tvET.setText("Tiempo aprox: "+currentWorkout.getEstimatedTimeInMinutes()+"'");
        tvExerciseCount.setText("Número de ejercicios: "+currentWorkout.getExercises().size());

        return view;
    }
}
