package com.motionishealth.application.training.android.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.motionishealth.application.training.android.DataManagement.WorkoutViewModel;
import com.motionishealth.application.training.android.POJOs.Workout;
import com.motionishealth.application.training.android.R;

import java.util.List;

public class WorkoutAdapter extends BaseAdapter {

    private WorkoutViewModel workoutViewModel;

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

    public WorkoutViewModel getWorkoutViewModel() {
        return workoutViewModel;
    }

    public void setWorkoutViewModel(WorkoutViewModel workoutViewModel) {
        this.workoutViewModel = workoutViewModel;
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
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.item_workout_master, viewGroup, false);
        }

        Workout currentWorkout = (Workout) getItem(i);
        final int position = i;

        ConstraintLayout clItemWorkoutParent = view.findViewById(R.id.clItemWorkoutParent);
        TextView tvName = view.findViewById(R.id.tvItemWorkoutMasterName);
        TextView tvET = view.findViewById(R.id.tvItemWorkoutMasterET);
        TextView tvExerciseCount = view.findViewById(R.id.tvItemWorkoutMasterExerciseNumber);
        ImageButton btDeleteWorkout = view.findViewById(R.id.ibItemWorkoutDelete);

        btDeleteWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               confirmDeleteItem(position);
            }
        });

        if (currentWorkout.getDifficulty() == 0){
            clItemWorkoutParent.setBackground(context.getResources().getDrawable(R.drawable.item_workout_background_easy));
        }else if (currentWorkout.getDifficulty() == 1){
            clItemWorkoutParent.setBackground(context.getResources().getDrawable(R.drawable.item_workout_background_medium));
        }else{
            clItemWorkoutParent.setBackground(context.getResources().getDrawable(R.drawable.item_workout_background_hard));
        }
        tvName.setText("Nombre: " + currentWorkout.getName());
        tvET.setText("Tiempo estimado de duración: " + currentWorkout.getEstimatedTimeInMinutes() + "'");
        tvExerciseCount.setText("Número de ejercicios: " + currentWorkout.getExercises().size());

        return view;
    }

    private void confirmDeleteItem(final int position){
        new AlertDialog.Builder(this.context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.fragments_workout_list_deleteDialogTitle)
                .setMessage(R.string.fragments_workout_list_deleteDialogMessage)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteWorkout(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteWorkout(int position){
        workoutViewModel.removeWorkoutFromList((Workout)getItem(position));
    }
}
