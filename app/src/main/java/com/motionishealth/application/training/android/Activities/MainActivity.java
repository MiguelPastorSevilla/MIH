package com.motionishealth.application.training.android.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.motionishealth.application.training.android.R;

public class MainActivity extends AppCompatActivity {

    private final static String TAG="MainActivity";

    private TextView tvSideMenuHeaderUserEmail;
    private FrameLayout flFragmentContainer;
    private DrawerLayout dlSideMenu;
    private NavigationView nvSideMenu;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Toolbar appActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        dlSideMenu = findViewById(R.id.dlSideMenu);
        flFragmentContainer = findViewById(R.id.flFragmentContainer);

        appActionBar = findViewById(R.id.abAppActionBar);
        setSupportActionBar(appActionBar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_sidemenu_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nvSideMenu = findViewById(R.id.nvSideMenu);
        View v = nvSideMenu.getHeaderView(0);
        tvSideMenuHeaderUserEmail = v.findViewById(R.id.tvSideMenuHeaderUserEmail);
        tvSideMenuHeaderUserEmail.setText(user.getEmail());


        nvSideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.optSideMenuRoutines:
                        //TODO: LLamar al fragment de rutinas
                        break;
                    case R.id.optSideMenuNutrition:
                        //Este fragment aun no está implementado.
                        break;
                    case R.id.optSideMenuSettings:
                        Log.i(TAG,"Cerrando sesión y volviendo a la pantalla de inicio");
                        auth.signOut();
                        finish();
                        break;
                     default:
                         break;
                }

                dlSideMenu.closeDrawers();
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (!dlSideMenu.isDrawerOpen(nvSideMenu)) {
                    dlSideMenu.openDrawer(GravityCompat.START);
                }else {
                    dlSideMenu.closeDrawers();
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
