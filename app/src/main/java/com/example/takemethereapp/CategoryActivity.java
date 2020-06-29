package com.example.takemethereapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CategoryActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setupBottomNavBar();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (CurrentUser == null) {
            sendToLogin();
        } else {
            // No user is signed in
        }
    }

    private void sendToLogin() {
        Intent goLogin = new Intent(CategoryActivity.this, LoginActivity.class);
        startActivity(goLogin);
        finish();
    }

    private void setupBottomNavBar(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent homeIntent = new Intent(CategoryActivity.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                        break;
                    case R.id.ic_category:
                        Intent ExerciseIntent = new Intent(CategoryActivity.this, CategoryActivity.class);
                        startActivity(ExerciseIntent);
                        finish();
                        break;
                    case R.id.ic_add:
                        Intent runIntent = new Intent(CategoryActivity.this, ComingSoon.class);
                        startActivity(runIntent);
                        finish();
                        break;
                    case R.id.ic_device:
                        Intent deviceIntent = new Intent(CategoryActivity.this, NotificationActivity.class);
                        startActivity(deviceIntent);
                        finish();
                        break;
                    case R.id.ic_profile:
                        Intent profileIntent = new Intent(CategoryActivity.this, AccountSettings.class);
                        startActivity(profileIntent);
                        finish();
                        break;
                }

                return false;
            }
        });
    }
}
