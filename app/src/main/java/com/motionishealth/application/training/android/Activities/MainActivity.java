package com.motionishealth.application.training.android.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.motionishealth.application.training.android.R;

public class MainActivity extends AppCompatActivity {

    private TextView tvUserUID;
    private FirebaseAuth auth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvUserUID = findViewById(R.id.tvUserUID);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        tvUserUID.setText("" + user.getUid());
    }
}
