package com.example.takemethereapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 0;
    private FirebaseAuth mAuth;
    private ImageView logout, gomainpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBottomNavBar();

        mAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.home_logout_btn);
//        gomainpage = findViewById(R.id.gomainpage);
//
//        gomainpage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent goSetup = new Intent(MainActivity.this, AccountSettings.class);
//                startActivity(goSetup);
//                finish();
//            }
//        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendToLogin();
            }
        });

        //=========================================================================================================
        //==========================================image slider===================================================
        //=========================================================================================================

        ViewPager2 locationViewPager = findViewById(R.id.locationViewPager);
        List<TravelLocation> travelLocations = new ArrayList<>();

        TravelLocation travelLocationWaterfalls =  new TravelLocation();
        travelLocationWaterfalls.imageUri = "https://firebasestorage.googleapis.com/v0/b/takemethereapp-b2646.appspot.com/o/sliderImages%2F139841-waterfalls_hero.jpg?alt=media&token=40914342-95c8-41dc-8be5-06460cb596a4";
        travelLocationWaterfalls.title = "Water Falls";
        travelLocationWaterfalls.location = "Sri Lanka";
        travelLocations.add(travelLocationWaterfalls);

        TravelLocation travelLocationCamping =  new TravelLocation();
        travelLocationCamping.imageUri = "https://firebasestorage.googleapis.com/v0/b/takemethereapp-b2646.appspot.com/o/sliderImages%2F2225850_17051516230052996076.jpg?alt=media&token=88872f59-0e2d-4cf0-b76b-f9b95ed23885";
        travelLocationCamping.title = "Camping";
        travelLocationCamping.location = "Sri Lanka";
        travelLocations.add(travelLocationCamping);

        TravelLocation travelLocationHiking =  new TravelLocation();
        travelLocationHiking.imageUri = "https://firebasestorage.googleapis.com/v0/b/takemethereapp-b2646.appspot.com/o/sliderImages%2FHiking-Cover-e1481132901201.jpg?alt=media&token=10dac393-3c17-44f2-8052-c58fd5870229";
        travelLocationHiking.title = "Hiking";
        travelLocationHiking.location = "Sri Lanka";
        travelLocations.add(travelLocationHiking);

        TravelLocation travelLocationClimbing =  new TravelLocation();
        travelLocationClimbing.imageUri = "https://firebasestorage.googleapis.com/v0/b/takemethereapp-b2646.appspot.com/o/sliderImages%2FHanthana-Mountain-Range-1.jpg?alt=media&token=ae5112a5-4776-4765-a86b-90c5e680ad16";
        travelLocationClimbing.title = "Climbing";
        travelLocationClimbing.location = "Sri Lanka";
        travelLocations.add(travelLocationClimbing);

        locationViewPager.setAdapter(new TravelLocationAdapter(travelLocations));

        locationViewPager.setClipToPadding(false);
        locationViewPager.setClipChildren(false);
        locationViewPager.setOffscreenPageLimit(3);
        locationViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f + r * 0.05f);
            }
        });

        locationViewPager.setPageTransformer(compositePageTransformer);

        //=========================================================================================================
        //========================================image slider ENDS here===========================================
        //=========================================================================================================
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
        Intent goLogin = new Intent(MainActivity.this, LoginActivity.class);
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
                        Intent homeIntent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                        break;
                    case R.id.ic_category:
                        Intent ExerciseIntent = new Intent(MainActivity.this, CategoryActivity.class);
                        startActivity(ExerciseIntent);
                        finish();
                        break;
                    case R.id.ic_add:
                        Intent runIntent = new Intent(MainActivity.this, AddPostActivity.class);
                        startActivity(runIntent);
                        finish();
                        break;
                    case R.id.ic_device:
                        Intent deviceIntent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(deviceIntent);
                        break;
                    case R.id.ic_profile:
                        Intent profileIntent = new Intent(MainActivity.this, AccountSettings.class);
                        startActivity(profileIntent);
                        finish();
                        break;
                }

                return false;
            }
        });
    }

}
