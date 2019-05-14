package com.motionishealth.application.training.android.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.motionishealth.application.training.android.Activities.MainActivity;
import com.motionishealth.application.training.android.R;


public class LoginActivity extends AppCompatActivity {
    //Log TAG
    public static final String TAG = "LoginActivity";
    //Constantes para preferencias.
    private static final String PREFERENCES = "LoginPreferences";
    private static final String EMAIL_PREFERENCES = "email";
    private static final String REMEMEBER_PREFERENCES = "rememberEmail";
    //Request code del registro
    public static final int REQUEST_CODE_REGISTER = 10001;
    //Variable de Firebase.
    private FirebaseAuth auth;
    //Elementos de interfaz.
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button btLogin;
    private Button btLoginRegister;
    private CheckBox cbRememberAccount;
    private TextView tvLoginError;
    //Preferencias.
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Inicilización de variables de interfaz
        tilEmail = findViewById(R.id.tilEmailLogin);
        tilPassword = findViewById(R.id.tilPasswordLogin);
        btLogin = findViewById(R.id.btLogin);
        btLoginRegister = findViewById(R.id.btLoginRegister);
        cbRememberAccount = findViewById(R.id.cbRememberAccount);
        tvLoginError = findViewById(R.id.tvLoginError);
        //Inicialización de Firebase
        auth = FirebaseAuth.getInstance();
        //Inicialización de referencias.
        preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        //Acción del botón de login
        btLogin.setOnClickListener(new View.OnClickListener() {
            /**
             * Click listener intentar logear con el email y la contraseña introducidas.
             * @param view vista.
             */
            @Override
            public void onClick(View view) {
                tryLogin();
            }
        });

        //Acción del botón de registro
        btLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });

        //Intento de recuperar el email de las preferencias
        tryGetEmailFromPreferences();
    }

    /**
     * Método para lanzar la actividad de registro.
     */
    private void startRegister() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Método para recuperar el email y el estado del checkBox de recordar
     * cuenta de las preferencias y asignar el email recuperado al campo de texto
     * y el estado al check box.
     */
    private void tryGetEmailFromPreferences() {
        boolean rememberAccount;
        rememberAccount = preferences.getBoolean(REMEMEBER_PREFERENCES, false);
        if (rememberAccount) {
            tilEmail.getEditText().setText(preferences.getString(EMAIL_PREFERENCES, ""));
            cbRememberAccount.setChecked(true);
            tilPassword.requestFocus();
            Log.i(TAG, "Preferencias recuperadas");
        }
    }

    /**
     * Método para intentar realizar un inicio de sesión.
     * Primero recupera el email y la contraseña y luego intenta
     * un inicio de sesión.
     */
    private void tryLogin() {
        Log.i(TAG, "Intento de login iniciado.");
        String email = tilEmail.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            tvLoginError.setVisibility(View.GONE);
                            actualizaPreferencias();
                            Log.i(TAG, "Login completado");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "Login fallido");
                            tvLoginError.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    private void actualizaPreferencias(){
        //Cambio de las preferencias en funcion del checkBox.
        SharedPreferences.Editor editor = preferences.edit();
        if (cbRememberAccount.isChecked()) {
            editor.putBoolean(REMEMEBER_PREFERENCES, true);
            editor.putString(EMAIL_PREFERENCES, tilEmail.getEditText().getText().toString());
            editor.apply();
            Log.i(TAG, "Preferencias actualizadas");
        } else {
            editor.putBoolean(REMEMEBER_PREFERENCES, false);
            editor.putString(EMAIL_PREFERENCES, "");
            editor.apply();
            Log.i(TAG, "Preferencias actualizadas");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Comprobamos al inicio de la aplicación si el usuario ya se encuentra logeado.
        //En este caso, lanzamos directamente la actividad principal.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Log.i(TAG, "Usuario logeado");
            /*
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            */
        } else {
            Log.i(TAG, "Usuario NO logeado");
        }
    }
}
