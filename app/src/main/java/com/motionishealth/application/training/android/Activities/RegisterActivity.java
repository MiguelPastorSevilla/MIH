package com.motionishealth.application.training.android.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.motionishealth.application.training.android.R;

import static com.motionishealth.application.training.android.Utils.Utils.checkEmail;
import static com.motionishealth.application.training.android.Utils.Utils.checkPasswords;

public class RegisterActivity extends AppCompatActivity {

    //Log TAG
    public static final String TAG = "RegisterActivity";

    //Elementos de interfaz
    private Button btRegister;
    private TextView tvAccountCreation;
    private FirebaseAuth auth;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilPasswordRepeat;

    //Variables de interfaz
    private String email;
    private String password;
    private String repeatedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Instancia del autentificador de Firebase
        auth = FirebaseAuth.getInstance();
        //Elementos de la interfaz
        btRegister = findViewById(R.id.btRegister);
        tilEmail = findViewById(R.id.tilEmailRegister);
        tilPassword = findViewById(R.id.tilPasswordRegister);
        tilPasswordRepeat = findViewById(R.id.tilPasswordRepeatRegister);
        tvAccountCreation = findViewById(R.id.tvAccountCreationStatus);
        //Listener del botón de registro
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryRegister();
            }
        });
    }

    /**
     * Método que intentará registrar un usuario nuevo con los datos de los edit text.
     * Realizará la comprobacón de validez tanto de email como contraseña. Si ambos resultan
     * ser validos, lanzará el registro de un nuevo usuario.
     */
    private void tryRegister() {
        //Recuperación de datos
        Log.i(TAG, "Recuperando datos de los editText");
        email = tilEmail.getEditText().getText().toString();
        password = tilPassword.getEditText().getText().toString();
        repeatedPassword = tilPasswordRepeat.getEditText().getText().toString();

        //Comprobación de email y contraseña.
        if (checkEmail(email, TAG)) {
            if (checkPasswords(password, repeatedPassword, TAG)) {
                //Caso de ambos ser correctos
                Log.i(TAG, "Lanzado registro con datos correctos, esperando resultado");
                //Mostramos el texto de creando cuenta
                //TODO: Cambiar por ProgressBar
                tvAccountCreation.setText(getResources().getString(R.string.login_creatingAccount));
                tvAccountCreation.setTextColor(Color.BLACK);
                //Lanzamiento de creación de usuario
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Si el usuario se crea correctamente, lanzamos la actividad principal con el usuario ya logeado.
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "Registro completado con éxito");
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_creatingAccountCompleted), Toast.LENGTH_LONG).show();
                                    Log.i(TAG, "Login lanzado");
                                    launchMainActivity(email, password);
                                } else {
                                    //Si no se crea usuario, mostramos error.
                                    Log.w(TAG, "Error en el registro");
                                    tvAccountCreation.setTextColor(Color.RED);
                                    tvAccountCreation.setText(getResources().getString(R.string.login_error_registerError));
                                }
                            }
                        });
            } else {
                //Error de contraseñas
                tvAccountCreation.setTextColor(Color.RED);
                tvAccountCreation.setText(getResources().getString(R.string.login_error_invalidPassword));
            }
        } else {
            //Error de invalidez de correo electronico.
            tvAccountCreation.setTextColor(Color.RED);
            tvAccountCreation.setText(getResources().getString(R.string.login_error_invalidEmail));
        }
    }


    /**
     * Método para lanzar la actividad principal una vez se logee un usuario con email y contraseña.
     *
     * @param email    email de login
     * @param password password de login
     */
    private void launchMainActivity(String email, String password) {
        //Intento de inicio de sesión
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si se inicia sesión
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Login completado");
                            //Lanzamos la actividad principal

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
