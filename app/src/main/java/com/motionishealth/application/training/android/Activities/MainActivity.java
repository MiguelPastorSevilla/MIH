package com.motionishealth.application.training.android.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.motionishealth.application.training.android.DataManagement.WorkoutViewModel;
import com.motionishealth.application.training.android.Fragments.WorkoutListFragment;
import com.motionishealth.application.training.android.POJOs.Workout;
import com.motionishealth.application.training.android.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG="MainActivity";

    private WorkoutViewModel workoutViewModel;
    private TextView tvSideMenuHeaderUserEmail;
    private FrameLayout flFragmentContainer;
    private DrawerLayout dlSideMenu;
    private NavigationView nvSideMenu;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Toolbar appActionBar;
    private ProgressBar pbLoadingMainList;

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
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        WorkoutListFragment workoutListFragment = new WorkoutListFragment();
                        workoutListFragment.setWorkouts(workoutViewModel.getWorkoutList().getValue());
                        fragmentTransaction.replace(R.id.flFragmentContainer,workoutListFragment);
                        fragmentTransaction.commit();
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

        pbLoadingMainList = findViewById(R.id.pbLoadingMainList);

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        workoutViewModel.getWorkoutList().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(@Nullable List<Workout> workouts) {
                pbLoadingMainList.setVisibility(View.GONE);
                Log.i(TAG,"Lista actualizada, progress bar quitada.");
            }
        });

        workoutViewModel.getWorkoutsFromFirebaseUser(user.getUid());

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
