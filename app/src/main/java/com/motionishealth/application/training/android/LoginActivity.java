package com.motionishealth.application.training.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    private static final String PREFERENCES = "LoginPreferences";
    private static final String EMAIL_PREFERENCES = "email";
    private static final String REMEMEBER_PREFERENCES = "rememberEmail";
    public static final int REQUEST_CODE_REGISTER = 10001;
    private FirebaseAuth authentificator;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private Button btLogin;
    private CheckBox cbRememberAccount;
    private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authentificator = FirebaseAuth.getInstance();
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        btLogin = findViewById(R.id.btLogin);
        cbRememberAccount = findViewById(R.id.cbRememberAccount);
        preferences = getSharedPreferences(PREFERENCES,MODE_PRIVATE);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmail()){
                    tryLogin();
                }
            }
        });
        tryGetEmailFromPreferences();
    }

    private void tryGetEmailFromPreferences() {
        boolean rememberAccount;
        rememberAccount = preferences.getBoolean(REMEMEBER_PREFERENCES,false);
        String email;
        if (rememberAccount){
            email = preferences.getString(EMAIL_PREFERENCES,"");
            tilEmail.getEditText().setText(email);
            cbRememberAccount.setChecked(true);
        }
    }

    private void tryLogin() {
        String email = tilEmail.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();
        SharedPreferences.Editor editor = preferences.edit();
        if (cbRememberAccount.isChecked()){
            editor.putBoolean(REMEMEBER_PREFERENCES,true);
            editor.putString(EMAIL_PREFERENCES,email);
            editor.apply();
        }else{
            editor.putBoolean(REMEMEBER_PREFERENCES,false);
            editor.putString(EMAIL_PREFERENCES,"");
            editor.apply();
        }

        Log.d(TAG,"EMAIL: "+email+" - PASSWORD: "+password);
    }

    private boolean checkEmail() {
        String email = tilEmail.getEditText().getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Log.i(TAG,"Correo válido");
            return true;
        }else{
            Log.w(TAG,"Correo no válido");
            tilEmail.setError(getResources().getString(R.string.error_invalidEmail));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){

            }
        }
    }
}
