package com.example.mobileapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        ListFragment listFragment = new ListFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        setCurrentFragment(homeFragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            final FragmentManager fm = getSupportFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.pageHome: ft.replace(R.id.flFrameLayout, homeFragment); // setCurrentFragment(homeFragment)
                case R.id.pageList: ft.replace(R.id.flFrameLayout, listFragment);//setCurrentFragment(listFragment)
                case R.id.pageProfile: ft.replace(R.id.flFrameLayout, profileFragment);
                default: break;//setCurrentFragment(profileFragment)
            }
            ft.addToBackStack(null);
            ft.commit();
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flFrameLayout, fragment);
        ft.commit();
    }
}