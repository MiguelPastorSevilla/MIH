package com.motionishealth.application.training.android.Activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.view.inputmethod.InputMethodManager;
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

    //Strings de claves para traspaso de información.
    private final static String TAG = "MainActivity";
    private final static String FIRST_ACTIVITY_CREATION = "com.motionishealth.application.training.android.FIRSTACTIVITYCREATION";
    private final static String CURRENT_TITLE = "com.motionishealth.application.training.android.CURRENTTITLE";

    //Elementos de la interfaz
    private TextView tvSideMenuHeaderUserEmail;
    private FrameLayout flFragmentContainer;
    private DrawerLayout dlSideMenu;
    private NavigationView nvSideMenu;
    private Toolbar appActionBar;

    //Elementos de Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;

    //Elementos para el control de la aplicación
    private WorkoutViewModel workoutViewModel;
    private Fragment currentFragment;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recuperación del usuario con el que se está accediendo
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //Recuperación de elementos de interfaz
        dlSideMenu = findViewById(R.id.dlSideMenu);
        flFragmentContainer = findViewById(R.id.flFragmentContainer);
        nvSideMenu = findViewById(R.id.nvSideMenu);

        appActionBar = findViewById(R.id.abAppActionBar);
        //Añadiendo la barra superior de la aplicación
        setSupportActionBar(appActionBar);

        //Añadiendo el menú "home" con el icono del menú "hamburguesa" para
        //abrir el menú de navegación lateral
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_sidemenu_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperación del TextView del menú de navegación lateral y asignación
        //del email del usuario actual como valor.
        View v = nvSideMenu.getHeaderView(0);
        tvSideMenuHeaderUserEmail = v.findViewById(R.id.tvSideMenuHeaderUserEmail);
        tvSideMenuHeaderUserEmail.setText(user.getEmail());

        //Eventos de clic de los distintos items del menú de navegación lateral.
        nvSideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //Caso de "Principal"
                    case R.id.optSideMenuHome:
                        callHomeFragment();
                        resetViewModelValues();
                        changeTitle();
                        break;
                    //Caso de "Rutinas"
                    case R.id.optSideMenuRoutines:
                        callWorkoutListFragment();
                        resetViewModelValues();
                        changeTitle();
                        break;
                    //Caso de "Cerrar sesión"
                    case R.id.optSideMenuLogout:
                        //En este caso, deslogeamos al usuario con la referencia autentificación
                        //de Firebase que tenemos guardada en "auth" con el método "signOut()" y
                        //finalizamos la actividad para volver al login.
                        Log.i(TAG, "Cerrando sesión y volviendo a la pantalla de inicio");
                        auth.signOut();
                        finish();
                        break;
                    default:
                        break;
                }
                //Al final y en cualquier caso, cerramos el menú y en caso de haber algún
                //teclado activo, se cierra.
                hideKeyboard();
                dlSideMenu.closeDrawers();
                return true;
            }
        });

        //Comprobamos que sea la primera ejecución de la actividad comprobando si el
        //"savedInstanceState" es nulo o no. Si es nulo es el primer lanzamiento, por lo que
        //lanzamos el fragment principal y seleccionamos la primera opción del menú de navegación
        //lateral, que la opción de "principal"
        if (savedInstanceState == null) {
            nvSideMenu.getMenu().getItem(0).setChecked(true);
            callHomeFragment();
            changeTitle();
        }

        //Recuperamos el ViewModel, que actuará como modelo de la aplicación.
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        //El uso que se hará del ViewModel es similar al que haría un controlador. Estará al tanto
        //de los cambios del modelo y realizará cambios en la interfaz acorde a dichos cambios.
        //En este caso, si el valor de "Rutina seleccionada" del modelo cambia a una distinta de nula,
        //llama al fragment detalle.
        workoutViewModel.getSelectedWorkout().observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                if (workout != null) {
                    callWorkoutDetailFragment();
                    changeTitle();
                }
            }
        });

        //Si cambia el valor de "Rutina a crear o editar" a uno distinto de nulo, y no estamos
        //aun ni editando ni creando una rutina, llama al fragment "Creación o edición de rutinas".
        workoutViewModel.getCreateEditWorkout().observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                if (workout != null && workoutViewModel.getCreatingWorkout().getValue() == false && workoutViewModel.getEditingWorkout().getValue() == false) {
                    callCreateEditFragment();
                    changeTitle();
                }
            }
        });

        //Si ha cambiado el valor de "Lista de rutinas cambiada" quiere decir que ha habido un cambio
        //en la lista, por lo que volvemos a llamar a la lista de rutinas.
        workoutViewModel.getWorkoutListChanged().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
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
        //Guardamos el título actual para sobrevivir a los cambios de orientación.
        outState.putString(CURRENT_TITLE, title);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Recuperamos el título actual para sobrevivir a los cambios de orientación.
        title = savedInstanceState.getString(CURRENT_TITLE);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Comprobamos que botón del menú se ha pulsado.
        switch (item.getItemId()) {
            //Si es el botón "Home" abrimos el menú de navegación lateral.
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

    /**
     * Método para llamar al fragment "Lista de rutinas", al que le pasamos el usuario de Firebase
     * para que pueda recuperar los datos del usuario.
     */
    private void callWorkoutListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WorkoutListFragment workoutListFragment = new WorkoutListFragment();
        workoutListFragment.setUser(this.user);
        currentFragment = workoutListFragment;
        fragmentTransaction.replace(R.id.flFragmentContainer, workoutListFragment, WorkoutListFragment.WORKOUT_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    /**
     * Método para llamar al fragment "Principal".
     */
    private void callHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        currentFragment = homeFragment;
        fragmentTransaction.replace(R.id.flFragmentContainer, homeFragment, HomeFragment.HOME_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    /**
     * Método para llamar al fragment "Detalles de rutinas". Este fragment lo añadiremos a la
     * pila de fragments para permitir la navegación "Lista de rutinas <-> Detalle de rutina".
     */
    private void callWorkoutDetailFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WorkoutDetailFragment detailFragment = new WorkoutDetailFragment();
        currentFragment = detailFragment;
        fragmentTransaction.replace(R.id.flFragmentContainer, detailFragment, WorkoutDetailFragment.WORKOUT_DETAIL_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Método para llamar al fragment de edición o creación de rutinas.
     */
    private void callCreateEditFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CreateEditWorkoutFragment createEditWorkoutFragment = new CreateEditWorkoutFragment();
        currentFragment = createEditWorkoutFragment;
        fragmentTransaction.replace(R.id.flFragmentContainer, createEditWorkoutFragment);
        fragmentTransaction.commit();
    }

    /**
     * Método para cargar el título en la barra superior dependiendo
     * del fragment que se esté mostrando actualmente.
     */
    private void changeTitle() {
        if (currentFragment instanceof WorkoutListFragment) {
            getSupportActionBar().setTitle(getResources().getString(R.string.sideMenu_options_routines));
            title = getSupportActionBar().getTitle().toString();
        } else if (currentFragment instanceof HomeFragment) {
            getSupportActionBar().setTitle(getResources().getString(R.string.sideMenu_options_home));
            title = getSupportActionBar().getTitle().toString();
        } else if (currentFragment instanceof WorkoutDetailFragment) {
            getSupportActionBar().setTitle(getResources().getString(R.string.fragments_titles_details));
            title = getSupportActionBar().getTitle().toString();
        } else if (currentFragment instanceof CreateEditWorkoutFragment) {
            getSupportActionBar().setTitle(getResources().getString(R.string.fragments_create_edit_title));
            title = getSupportActionBar().getTitle().toString();
        }
    }

    @Override
    public void onBackPressed() {
        //Cuando se pulse el botón de atrás, si el fragment actual es un fragment de "Detalles de rutina"
        //vaciamos la pila de fragments para volver al fragment "Lista de rutinas" y reiniciamos la rutina seleccionada.
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0 && currentFragment instanceof WorkoutDetailFragment) {
            workoutViewModel.setSelectedWorkout(null);
            emptyFragmentBackStack();
            currentFragment = fragmentManager.findFragmentByTag(WorkoutListFragment.WORKOUT_FRAGMENT_TAG);
            changeTitle();
        }
    }

    /**
     * Método para vaciar la pila de fragments.
     */
    private void emptyFragmentBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    /**
     * Método para reiniciar los valores del modelo.
     */
    private void resetViewModelValues() {
        workoutViewModel.setSelectedWorkout(null);
        workoutViewModel.getCreateEditWorkout().setValue(null);
        workoutViewModel.getEditingWorkout().setValue(false);
        workoutViewModel.getCreatingWorkout().setValue(false);
    }

    /**
     * Método para esconder el teclado si estuviese activo.
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
