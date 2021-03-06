package com.belajar.newportofolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Initialize
        BottomNavigationView bnav = findViewById(R.id.bottomNavigationView);

//        Set first selected
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ProfileFragment()).commit();

//        Perform ItemSelectedListener
        bnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;

                switch (item.getItemId()) {
                    case R.id.profileFragment:
                        selected = new ProfileFragment();
                        break;
                    case R.id.appsFragment:
                        selected = new AppsFragment();
                        break;
                    case R.id.skillsFragment:
                        selected = new SkillsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selected).commit();

                return true;
            }
        });
    }
}