package com.example.takemethereapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageUri = null;
    private EditText setupName;
    private Button setupButton;
    private ProgressBar setupProgreess;

    private String user_id;
    private boolean isChanged = false;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

        setupImage = findViewById(R.id.setup_image);
        setupName = findViewById(R.id.setup_name);
        setupButton = findViewById(R.id.setup_submit_btn);
        setupProgreess = findViewById(R.id.setup_progressbar);

//        setupProgreess.setVisibility(View.VISIBLE);
//        setupButton.setEnabled(false);
//
//        //=========================edit profile option,
//        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    if (task.getResult().exists()){
//                        String name = task.getResult().getString("name");
//                        String image = task.getResult().getString("image");
//
//                        mainImageUri = Uri.parse(image);
//
//                        setupName.setText(name);
//                        RequestOptions placeholder = new RequestOptions();
//                        placeholder.placeholder(R.drawable.setup_camera);
//                        Glide.with(SetupActivity.this).setDefaultRequestOptions(placeholder).load(image).into(setupImage);
//                    }
//                }else {
//                    String err = task.getException().getMessage();
//                    Toast.makeText(SetupActivity.this,"Firestore retrive errr" + err, Toast.LENGTH_LONG).show();
//                }
//                setupProgreess.setVisibility(View.INVISIBLE);
//                setupButton.setEnabled(true);
//            }
//        });

        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = setupName.getText().toString();

//                if (isChanged) {

                    if (!TextUtils.isEmpty(user_name) && mainImageUri != null) {

                        user_id = mAuth.getCurrentUser().getUid();
                        setupProgreess.setVisibility(View.VISIBLE);
                        StorageReference imagePath = storageReference.child("profile_images").child(user_id + ".jpg");

                        final UploadTask uploadTask = imagePath.putFile(mainImageUri);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String download_url = uri.toString();
                                        storeFirebaseStore(download_url, user_name);
                                    }

                                });

                            }
                        });
                    }
            }
        });

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                        Toast.makeText(SetupActivity.this,"Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else{
//                        Toast.makeText(SetupActivity.this,"Already have Permission", Toast.LENGTH_LONG).show();

                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(SetupActivity.this);
                    }

                }
                else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(SetupActivity.this);
                }
            }
        });
    }

    private void storeFirebaseStore(String download_url, String user_name) {

        Map<String,String> userMap = new HashMap<>();
        userMap.put("name",user_name);
        userMap.put("image",download_url);

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    sendToMain();
                    setupProgreess.setVisibility(View.INVISIBLE);

                }else {
                    String err = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this,"Firestore error " + err, Toast.LENGTH_LONG).show();
                    setupProgreess.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mainImageUri = result.getUri();
                setupImage.setImageURI(mainImageUri);
                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    private void sendToMain() {
        Intent goMain = new Intent(SetupActivity.this, MainActivity.class);
        startActivity(goMain);
        finish();
    }
}
