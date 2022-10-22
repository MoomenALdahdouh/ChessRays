package com.moomen.chessrays.ui.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moomen.chessrays.R;
import com.moomen.chessrays.utils.PreferenceUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarConfiguration mAppBarConfiguration;
    private TextView textViewUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceUtils.saveAccountType("Classical", getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Navigation drawer
        DrawerLayout drawer = findViewById(R.id.container);
        NavigationView navigationView = findViewById(R.id.nav_view_drawer);
        //Click on sign out
        //navigationView.setNavigationItemSelectedListener(this);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_opening, R.id.navigation_middle, R.id.navigation_end, R.id.navigation_themes)
                .setDrawerLayout(drawer)
                .build();


        //Bottom navigation
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        //
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //----get user name
        View header = navigationView.getHeaderView(0);
        textViewUserName = header.findViewById(R.id.textView_user_name_id);
        textViewUserName.setText(PreferenceUtils.getName(getApplicationContext()));


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //Now it's not working neglit it
    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_sign_out) {
            PreferenceUtils.saveName("", getApplicationContext());
            startActivity(new Intent(getApplicationContext(), IntroActivity.class));
            finish();
        }
        return true;
    }
}