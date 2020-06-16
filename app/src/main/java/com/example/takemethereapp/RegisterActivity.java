package com.example.takemethereapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPass;
    private EditText regConfirmPass;
    private Button regBtn, reg_login_btn;
    private ProgressBar registerProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        regEmail = (EditText) findViewById(R.id.setup_name);
        regPass = (EditText) findViewById(R.id.reg_password);
        regConfirmPass = (EditText) findViewById(R.id.reg_confirm_pass);
        regBtn = (Button) findViewById(R.id.reg_btn);
        reg_login_btn = (Button) findViewById(R.id.reg_login_btn);
        registerProgress = (ProgressBar) findViewById(R.id.register_progressbar);

        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = regEmail.getText().toString();
                String pass = regPass.getText().toString();
                String conPass = regConfirmPass.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(conPass)) {
                    registerProgress.setVisibility(View.VISIBLE);

                    if (pass.equals(conPass)) {
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Intent goSetuo = new Intent(RegisterActivity.this, SetupActivity.class);
                                    startActivity(goSetuo);
                                    finish();
                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this,"invalid registration",Toast.LENGTH_LONG).show();

                                }
                                registerProgress.setVisibility(View.INVISIBLE);
                            }
                        });

                    } else {
                    Toast.makeText(RegisterActivity.this,"Passwords are not match",Toast.LENGTH_LONG).show();

                }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser CurrentUser = mAuth.getCurrentUser();
        if (CurrentUser != null) {
            sendToMain();
        } else {
            // No user is signed in
        }
    }

    private void sendToMain() {
        Intent goMain = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(goMain);
        finish();
    }
}
