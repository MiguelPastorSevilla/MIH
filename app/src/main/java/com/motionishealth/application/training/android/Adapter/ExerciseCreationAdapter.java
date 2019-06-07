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

    //TAG Del adaptador.
    private static final String TAG = "ExerciseCreationAdapter";

    //Contexto del adaptador.
    private Context context;
    //Lista de ejercicios a mostrar.
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
        //Vista a inflar con el ejercicio número "i" de la lista de ejercicios.
        View v = LayoutInflater.from(this.context).inflate(R.layout.item_workout_create_edit_exercise, viewGroup, false);
        //Ejercicio número "i".
        Exercise currentExercise = (Exercise) getItem(i);

        Log.i(TAG, currentExercise.getName() + " - " + i);

        //Elementos de la vista
        TextView tvItemExerciseNameCreateEdit = v.findViewById(R.id.tvItemExerciseNameCreateEdit);
        TextView tvItemExerciseRepsCreateEdit = v.findViewById(R.id.tvItemExerciseRepsCreateEdit);
        TextView tvItemExerciseSetsCreateEdit = v.findViewById(R.id.tvItemExerciseSetsCreateEdit);
        ImageButton btItemExerciseAddExercise = v.findViewById(R.id.btItemExerciseCancelExercise);

        //Variable para poder recuperar la posición del propio ejercicio a eliminar en el botón de eliminar ejercicio.
        final int position = i;

        //Listener para eliminar el propio ejercicio del adaptador al pulsar el botón de eliminar ejercicio.
        btItemExerciseAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
            }
        });

        //Asignación de valores a los campos de texto con los datos del ejercicio.
        tvItemExerciseNameCreateEdit.setText(currentExercise.getName());
        tvItemExerciseRepsCreateEdit.setText(currentExercise.getReps().toString());
        tvItemExerciseSetsCreateEdit.setText(currentExercise.getSets().toString());
        return v;
    }

    /**
     * Método para eliminar un ejercicio del adaptador.
     * @param position Posición del ejercicio a eliminar
     */
    private void removeItem(int position) {
        exercises.remove(position);
        notifyDataSetChanged();
    }

    /**
     * Setter de lista de ejercicios
     * @param exercises Lista de ejercicios
     */
    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }
}
