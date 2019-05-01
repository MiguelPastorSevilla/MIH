package com.motionishealth.application.training.android;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    private FirebaseAuth authentificator;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authentificator = FirebaseAuth.getInstance();
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmail()){
                    tryLogin();
                }
            }
        });
    }

    private void tryLogin() {
        String email = tilEmail.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();

        Log.d(TAG,"EMAIL: "+email+" - PASSWORD: "+password);
    }

    private boolean checkEmail() {
        String email = tilEmail.getEditText().getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Log.i(TAG,"Correo válido");
            return true;
        }else{
            Log.w(TAG,"Correo no válido");
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authentificator.getCurrentUser();
        if (currentUser!=null)
        {
            Log.i(TAG,"Usuario logeado");
        }else
        {
            Log.i(TAG,"Usuario NO logeado");
        }
    }
}
