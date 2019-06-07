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

    //Tag
    public static final String TAG = "CreateEditWorkoutFrag";
    //ViewModel
    private WorkoutViewModel workoutViewModel;

    //Elementos de interfaz
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

    //Adaptador de ejercicios
    private ExerciseCreationAdapter exerciseCreationAdapter;

    public CreateEditWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Referencia del ViewModel de la aplicación
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutViewModel.class);
        //Inicialización del adaptador de ejercicios
        exerciseCreationAdapter = new ExerciseCreationAdapter(getContext(), new ArrayList<Exercise>());
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //Vista a inflar.
        View v = inflater.inflate(R.layout.fragment_create_edit_workout, container, false);

        //Recuperación de elementos de interfaz.
        etItemWorkoutNameCreateEdit = v.findViewById(R.id.etItemWorkoutNameCreateEdit);
        etItemWorkoutDescriptionCreateEdit = v.findViewById(R.id.etItemWorkoutDescriptionCreateEdit);
        etItemWorkoutETCreateEdit = v.findViewById(R.id.etItemWorkoutETCreateEdit);
        rgItemWorkoutDifficulty = v.findViewById(R.id.rgItemWorkoutDifficulty);

        lvExerciseList = v.findViewById(R.id.lvExerciseCreationList);

        tvItemExerciseNameHeaderCreateEdit = v.findViewById(R.id.tvItemExerciseNameHeaderCreateEdit);
        tvItemExerciseRepsHeaderCreateEdit = v.findViewById(R.id.tvItemExerciseRepsHeaderCreateEdit);
        tvItemExerciseSetsHeaderCreateEdit = v.findViewById(R.id.tvItemExerciseSetsHeaderCreateEdit);

        //Listener para transmitir el foco al EditText del nombre del ejercicio.
        tvItemExerciseNameHeaderCreateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etItemExerciseNameCreateEdit.requestFocus();
            }
        });

        //Listener para transmitir el foco al EditText de las repeticiones del ejercicio.
        tvItemExerciseRepsHeaderCreateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etItemExerciseRepsCreateEdit.requestFocus();
            }
        });

        //Listener para transmitir el foco al EditText de las series del ejercicio.
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

        //Listener para recoger la pulsación del botón del teclado "Listo" y llamar al botón de añadir
        //ejercicio
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

        //Listener del botón de añadir ejercicio.
        btItemExerciseAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recogida del nombre introducido en el ejercicio.
                String exerciseName = etItemExerciseNameCreateEdit.getText().toString();
                Long exerciseReps = (long) 0;
                Long exerciseSets = (long) 0;
                try {
                    //Intento de asignación de repeticiones y series.
                    exerciseReps = Long.parseLong(etItemExerciseRepsCreateEdit.getText().toString());
                    exerciseSets = Long.parseLong(etItemExerciseSetsCreateEdit.getText().toString());
                } catch (Exception e) {
                    //Si se recoge una excepción es porque o las series o las repeticiones estaban vacías y se cancela
                    //la adición del ejercicio a la lista.
                    Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_exercises_too_long), Toast.LENGTH_SHORT
                    ).show();
                    Log.e(TAG, e.getMessage());
                    return;
                }
                //Comprobación de que el nombre del ejercicio no esté en blacno.
                if (!exerciseName.trim().isEmpty()) {
                    //Si no esta en blanco, se crea un nuevo ejercicio.
                    Exercise exercise = new Exercise(exerciseName, exerciseReps, exerciseSets);
                    //Se añade a la lista.
                    exerciseCreationAdapter.getExercises().add(exercise);
                    exerciseCreationAdapter.notifyDataSetChanged();
                    //Se reinician los campos de creación.
                    etItemExerciseNameCreateEdit.setText("");
                    etItemExerciseRepsCreateEdit.setText("");
                    etItemExerciseSetsCreateEdit.setText("");
                    etItemExerciseNameCreateEdit.requestFocus();
                }else{
                    //Si el nombre está vacío, no se añade y se muestra un error.
                    Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_exercises_no_name), Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        //Listener del botón flotante de guardado.
        fabSaveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySaveWorkout();
            }
        });

        //Recuperación de la rutina que vamos a crear o modificar del modelo.
        Workout workoutReceived = workoutViewModel.getCreateEditWorkout().getValue();

        try {
            //Comprobación de nombre de rutina nulo.
            if (workoutReceived.getName() == null) {
                //Si el nombre es nulo, estaremos creando una nueva rutina, lo reflejamos en el modelo.
                workoutViewModel.getCreatingWorkout().setValue(true);
            } else {
                //Si el nombre no es nulo, estaremos modificando una rutina, lo reflejamos en el modelo.
                workoutViewModel.getEditingWorkout().setValue(true);
            }
        } catch (NullPointerException npe) {
            //Si llegamos a un puntero nulo, quiere decir que estamos creando una rutina nueva.
            workoutViewModel.getCreatingWorkout().setValue(true);
        }

        //Comprobamos si estamos editando una rutina para asginar sus valores a los campos de texto correspondientes.
        if (workoutViewModel.getEditingWorkout().getValue()) {
            //Cambiamos nombre.
            etItemWorkoutNameCreateEdit.setText(workoutReceived.getName());
            //Cambiamos descripción.
            etItemWorkoutDescriptionCreateEdit.setText(workoutReceived.getDescription());
            //Cambiamos tiempo estimado de duración.
            etItemWorkoutETCreateEdit.setText(workoutReceived.getEstimatedTimeInMinutes().toString());
            //Marcamos la dificultad correspondiente.
            if (workoutReceived.getDifficulty() == (long) 0) {
                rgItemWorkoutDifficulty.check(R.id.rbItemWorkoutDifficultyEasy);
            } else if (workoutReceived.getDifficulty() == (long) 1) {
                rgItemWorkoutDifficulty.check(R.id.rbItemWorkoutDifficultyMedium);
            } else {
                rgItemWorkoutDifficulty.check(R.id.rbItemWorkoutDifficultyHard);
            }
            //Cargamos un nuevo adaptador con la lista de ejercicios recibida.
            exerciseCreationAdapter = new ExerciseCreationAdapter(getContext(), (ArrayList<Exercise>) workoutReceived.getExercises());
        }

        //Cargamos el adaptador a la lista de ejercicios.
        lvExerciseList.setAdapter(exerciseCreationAdapter);

        return v;
    }

    /**
     * Método para comprobar la integridad de los datos de la rutina y si son válidos, guardarla.
     */
    private void trySaveWorkout() {
        Log.i(TAG, "Intento de guardar rutina");
        //Comprobación del nombre.
        if (etItemWorkoutNameCreateEdit.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_name), Toast.LENGTH_SHORT
            ).show();
            Log.w(TAG, "Nombre de rutina vacío");
            return;
        }
        //Comprobación de la descripción.
        if (etItemWorkoutDescriptionCreateEdit.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_description), Toast.LENGTH_SHORT
            ).show();
            Log.w(TAG, "Descripción de rutina vacía ");
            return;
        }
        //Comprobación del tiempo estimado vacío.
        if (etItemWorkoutETCreateEdit.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_et), Toast.LENGTH_SHORT
            ).show();
            Log.w(TAG, "Tiempo estimado de rutina vacío");
            return;
        }
        try{
            //Comprobación del tiempo estimado sea 0.
            if (Long.parseLong(etItemWorkoutETCreateEdit.getText().toString()) <= 0) {
                Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_et_zero), Toast.LENGTH_SHORT
                ).show();
                Log.w(TAG, "Tiempo estimado de rutina es 0");
                return;
            }
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
            //Si llegamos a esta excepción quiere decir que el tiempo estimado era demasiado largo.
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_et_too_long), Toast.LENGTH_SHORT
            ).show();
            return;
        }
        //Recogida de la dificultad de la rutina en funcion de la opción marcada.
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
                //En caso de no haber ninguna pulsada.
                Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_difficulty), Toast.LENGTH_SHORT
                ).show();
                Log.w(TAG, "Ninguna dificultad seleccionada");
                return;
        }

        //Comprobación de que haya al menos 1 ejercicio creado.
        if (exerciseCreationAdapter.getExercises().size() <= 0) {
            Toast.makeText(getContext(), getResources().getString(R.string.fragments_create_edit_error_exercises), Toast.LENGTH_SHORT
            ).show();
            Log.w(TAG, "Ningun ejercicio creado");
            return;
        }

        //Rutina vacía a la que almacenaremos los datos.
        Workout workout;

        //Comprobación de si estamos creando una rutina nueva.
        if (workoutViewModel.getCreatingWorkout().getValue()) {
            //Inicializamos la rutina nueva si la estamos creando de 0.
            workout = new Workout(etItemWorkoutNameCreateEdit.getText().toString(),
                    etItemWorkoutDescriptionCreateEdit.getText().toString(),
                    Long.parseLong(etItemWorkoutETCreateEdit.getText().toString()),
                    workoutDifficulty,
                    exerciseCreationAdapter.getExercises());
        } else {
            //Si la estamos modificando, igualamos su valor y cambiamos sus atributos.
            workout = workoutViewModel.getCreateEditWorkout().getValue();
            workout.setName(etItemWorkoutNameCreateEdit.getText().toString());
            workout.setDescription(etItemWorkoutDescriptionCreateEdit.getText().toString());
            workout.setEstimatedTimeInMinutes(Long.parseLong(etItemWorkoutETCreateEdit.getText().toString()));
            workout.setDifficulty(workoutDifficulty);
            workout.setExercises(exerciseCreationAdapter.getExercises());
        }

        //Llamamos al método del modelo para añadir una rutina nueva.
        workoutViewModel.addWorkoutToList(workout);
        //Avisamos que la lista de rutinas ha cambiado.
        workoutViewModel.getWorkoutListChanged().setValue(true);
        //Reiniciamos los valores del modelo.
        workoutViewModel.getCreateEditWorkout().setValue(null);
        workoutViewModel.getEditingWorkout().setValue(false);
        workoutViewModel.getCreatingWorkout().setValue(false);
    }
}
