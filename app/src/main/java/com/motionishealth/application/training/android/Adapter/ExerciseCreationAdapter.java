package com.motionishealth.application.training.android.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.motionishealth.application.training.android.POJOs.Exercise;
import com.motionishealth.application.training.android.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseCreationAdapter extends BaseAdapter {

    private static final String TAG = "ExerciseCreationAdapter";

    private Context context;
    private ArrayList<Exercise> exercises;

    public ExerciseCreationAdapter(Context context, ArrayList<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    public ExerciseCreationAdapter(Context context) {
        this.context = context;
        this.exercises = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public Object getItem(int i) {
        return exercises.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(this.context).inflate(R.layout.item_workout_create_edit_exercise,viewGroup,false);

        Exercise currentExercise = (Exercise)getItem(i);
        Log.i(TAG,currentExercise.getName()+" - "+i);
        final int position = i;

        TextView tvItemExerciseNameCreateEdit = v.findViewById(R.id.tvItemExerciseNameCreateEdit);
        TextView tvItemExerciseRepsCreateEdit = v.findViewById(R.id.tvItemExerciseRepsCreateEdit);
        TextView tvItemExerciseSetsCreateEdit = v.findViewById(R.id.tvItemExerciseSetsCreateEdit);
        ImageButton btItemExerciseAddExercise = v.findViewById(R.id.btItemExerciseCancelExercise);

        btItemExerciseAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
            }
        });

        tvItemExerciseNameCreateEdit.setText(currentExercise.getName());
        tvItemExerciseRepsCreateEdit.setText(currentExercise.getReps().toString());
        tvItemExerciseSetsCreateEdit.setText(currentExercise.getSets().toString());
        return v;
    }

    private void removeItem(int position){
        exercises.remove(position);
        notifyDataSetChanged();
    }



}
