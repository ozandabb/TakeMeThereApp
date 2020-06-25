package com.example.takemethereapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettings extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 4;

    private CircleImageView accountImage;
    private Uri mainImageUri = null;
    private EditText accountName;
    private Button accountButton;
    private ProgressBar accountProgreess;
    private String user_id;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        setupBottomNavBar();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

        accountImage = findViewById(R.id.account_image);
        accountName = findViewById(R.id.account_name);
        accountButton = findViewById(R.id.account_submit_btn);
        accountProgreess = findViewById(R.id.account_progressbar);

        accountProgreess.setVisibility(View.VISIBLE);
        accountButton.setEnabled(false);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");

//                        mainImageUri = Uri.parse(image);

                        accountName.setText(name);
                        RequestOptions placeholder = new RequestOptions();
                        placeholder.placeholder(R.drawable.setup_camera);
                        Glide.with(AccountSettings.this).setDefaultRequestOptions(placeholder).load(image).into(accountImage);
                    }
                }else {
                    String err = task.getException().getMessage();
                    Toast.makeText(AccountSettings.this,"Firestore retrive errr" + err, Toast.LENGTH_LONG).show();
                }
                accountProgreess.setVisibility(View.INVISIBLE);
                accountButton.setEnabled(true);
            }
        });
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
        Intent goLogin = new Intent(AccountSettings.this, LoginActivity.class);
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
                        Intent homeIntent = new Intent(AccountSettings.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                        break;
                    case R.id.ic_category:
                        Intent ExerciseIntent = new Intent(AccountSettings.this, CategoryActivity.class);
                        startActivity(ExerciseIntent);
                        finish();
                        break;
                    case R.id.ic_add:
                        Intent runIntent = new Intent(AccountSettings.this, ComingSoon.class);
                        startActivity(runIntent);
                        finish();
                        break;
                    case R.id.ic_device:
                        Intent deviceIntent = new Intent(AccountSettings.this, NotificationActivity.class);
                        startActivity(deviceIntent);
                        finish();
                        break;
                    case R.id.ic_profile:
                        Intent profileIntent = new Intent(AccountSettings.this, AccountSettings.class);
                        startActivity(profileIntent);
                        finish();
                        break;
                }

                return false;
            }
        });
    }
}
