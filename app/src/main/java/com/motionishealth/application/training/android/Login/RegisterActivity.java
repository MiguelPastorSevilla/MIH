package com.motionishealth.application.training.android.Login;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.motionishealth.application.training.android.R;

public class RegisterActivity extends AppCompatActivity {

    private Button btRegister;
    private TextView tvAccountCreation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btRegister = findViewById(R.id.btRegister);
        tvAccountCreation = findViewById(R.id.tvAccountCreation);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryRegister();
            }
        });
    }

    private void tryRegister() {
        tvAccountCreation.setText(getResources().getString(R.string.login_creatingAccount));
        //TODO: Implementar el registro
        //Si el registro se completa vamos a la actividad principal

        //Si el registro falla
            tvAccountCreation.setTextColor(Color.RED);
            tvAccountCreation.setText(getResources().getString(R.string.login_error_registerError));
    }


}
