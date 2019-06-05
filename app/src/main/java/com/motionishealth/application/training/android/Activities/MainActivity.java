package com.motionishealth.application.training.android.Activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.motionishealth.application.training.android.DataManagement.WorkoutViewModel;
import com.motionishealth.application.training.android.Fragments.CreateEditWorkoutFragment;
import com.motionishealth.application.training.android.Fragments.HomeFragment;
import com.motionishealth.application.training.android.Fragments.WorkoutDetailFragment;
import com.motionishealth.application.training.android.Fragments.WorkoutListFragment;
import com.motionishealth.application.training.android.POJOs.Workout;
import com.motionishealth.application.training.android.R;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private final static String FIRST_ACTIVITY_CREATION = "com.motionishealth.application.training.android.FIRSTACTIVITYCREATION";
    private final static String CURRENT_TITLE = "com.motionishealth.application.training.android.CURRENTTITLE";
    private final static String MENU_SAVE_SHOWN = "com.motionishealth.application.training.android.SAVEICON";

    private TextView tvSideMenuHeaderUserEmail;
    private FrameLayout flFragmentContainer;
    private DrawerLayout dlSideMenu;
    private NavigationView nvSideMenu;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Toolbar appActionBar;
    private WorkoutViewModel workoutViewModel;

    private Fragment currentFragment;
    private String title;

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
                switch (menuItem.getItemId()) {
                    case R.id.optSideMenuHome:
                        callHomeFragment();
                        workoutViewModel.setSelectedWorkout(null);
                        workoutViewModel.setEditingWorkout(new MutableLiveData<Boolean>());
                        changeTitle();
                        break;
                    case R.id.optSideMenuRoutines:
                        callWorkoutListFragment();
                        workoutViewModel.setSelectedWorkout(null);
                        workoutViewModel.setEditingWorkout(new MutableLiveData<Boolean>());
                        changeTitle();
                        break;
                    case R.id.optSideMenuLogout:
                        Log.i(TAG, "Cerrando sesión y volviendo a la pantalla de inicio");
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
        if (savedInstanceState == null) {
            nvSideMenu.getMenu().getItem(0).setChecked(true);
            callHomeFragment();
            changeTitle();
        }

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        workoutViewModel.getSelectedWorkout().observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                if (workout != null){
                    callWorkoutDetailFragment();
                    changeTitle();
                }
            }
        });

        workoutViewModel.getCreateEditWorkout().observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                if (workout!=null && workoutViewModel.getCreateEditWorkout()!=null){
                    callCreateEditFragment();
                    changeTitle();
                }
            }
        });

        workoutViewModel.getWorkoutListChanged().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean!=null && aBoolean){
                    callWorkoutListFragment();
                    workoutViewModel.setSelectedWorkout(null);
                    changeTitle();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FIRST_ACTIVITY_CREATION, false);
        outState.putString(CURRENT_TITLE, title);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        title = savedInstanceState.getString(CURRENT_TITLE);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!dlSideMenu.isDrawerOpen(nvSideMenu)) {
                    dlSideMenu.openDrawer(GravityCompat.START);
                } else {
                    dlSideMenu.closeDrawers();
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callWorkoutListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WorkoutListFragment workoutListFragment = new WorkoutListFragment();
        workoutListFragment.setUser(this.user);
        currentFragment = workoutListFragment;
        fragmentTransaction.replace(R.id.flFragmentContainer, workoutListFragment, WorkoutListFragment.WORKOUT_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    private void callHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        currentFragment = homeFragment;
        fragmentTransaction.replace(R.id.flFragmentContainer, homeFragment, HomeFragment.HOME_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    private void callWorkoutDetailFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WorkoutDetailFragment detailFragment = new WorkoutDetailFragment();
        currentFragment = detailFragment;
        fragmentTransaction.replace(R.id.flFragmentContainer, detailFragment, WorkoutDetailFragment.WORKOUT_DETAIL_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void callCreateEditFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CreateEditWorkoutFragment createEditWorkoutFragment = new CreateEditWorkoutFragment();
        currentFragment = createEditWorkoutFragment;
        fragmentTransaction.replace(R.id.flFragmentContainer, createEditWorkoutFragment);
        fragmentTransaction.commit();
    }

    private void changeTitle(){
        if (currentFragment instanceof  WorkoutListFragment){
            getSupportActionBar().setTitle(getResources().getString(R.string.sideMenu_options_routines));
            title = getSupportActionBar().getTitle().toString();
        }else if (currentFragment instanceof  HomeFragment){
            getSupportActionBar().setTitle(getResources().getString(R.string.sideMenu_options_home));
            title = getSupportActionBar().getTitle().toString();
        }else if (currentFragment instanceof  WorkoutDetailFragment){
            getSupportActionBar().setTitle(getResources().getString(R.string.fragments_titles_details));
            title = getSupportActionBar().getTitle().toString();
        }else if (currentFragment instanceof CreateEditWorkoutFragment){
            getSupportActionBar().setTitle(getResources().getString(R.string.fragments_create_edit_title));
            title = getSupportActionBar().getTitle().toString();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount()>0 && currentFragment instanceof WorkoutDetailFragment){
            workoutViewModel.setSelectedWorkout(null);
            emptyFragmentBackStack();
            currentFragment = fragmentManager.findFragmentByTag(WorkoutListFragment.WORKOUT_FRAGMENT_TAG);
            changeTitle();
        }
    }

    private void emptyFragmentBackStack(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }
}
