package com.example.takemethereapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 0;
    private FirebaseAuth mAuth;
    private ImageView logout;
    private TextView arrow;

    private RecyclerView blog_list_view;
    private BlogRecylerAdapter blogRecylerAdapter;
    private List<BlogPost> blogLost;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBottomNavBar();

//        blog_list_view = findViewById(R.id.blog_list_view);
//        blogLost = new ArrayList<>();
//        blogRecylerAdapter = new BlogRecylerAdapter(blogLost);
//
//        blog_list_view.setLayoutManager(new LinearLayoutManager(this));
//        blog_list_view.setAdapter(blogRecylerAdapter);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        logout = findViewById(R.id.home_logout_btn);
        arrow = findViewById(R.id.arrowHome);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goCategoryPage = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(goCategoryPage);
                finish();

            }
        });

//        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
//                    if (doc.getType() == DocumentChange.Type.ADDED){
//                        BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
//                        blogLost.add(blogPost);
//                        blogRecylerAdapter.notifyDataSetChanged();
//                    }
//                }
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

//        locationViewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent goCategoryPage = new Intent(MainActivity.this, CategoryActivity.class);
//                startActivity(goCategoryPage);
//                finish();
//            }
//        });

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
                        break;
                    case R.id.ic_category:
                        Intent ExerciseIntent = new Intent(MainActivity.this, CategoryActivity.class);
                        startActivity(ExerciseIntent);
                        finish();
                        break;
                    case R.id.ic_add:
                        Intent runIntent = new Intent(MainActivity.this, ComingSoon.class);
                        startActivity(runIntent);
                        finish();
                        break;
                    case R.id.ic_device:
                        Intent deviceIntent = new Intent(MainActivity.this, NotificationActivity.class);
                        startActivity(deviceIntent);
                        finish();
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
