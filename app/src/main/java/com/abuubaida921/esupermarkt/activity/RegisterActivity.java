package com.abuubaida921.esupermarkt.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.Config;
import com.abuubaida921.esupermarkt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class RegisterActivity extends Activity {

    TextView privacy_policy;
    Button signUp_btn;
    EditText full_name,usr_email,usr_pass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        usr_pass=findViewById(R.id.usr_pass);
        usr_email=findViewById(R.id.usr_email);
        full_name=findViewById(R.id.full_name);
        signUp_btn=findViewById(R.id.signUp_btn);
        privacy_policy=findViewById(R.id.privacy_policy);
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(Config.appPrivacyUrl));
                startActivity(openURL);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _fullName = full_name.getText().toString().trim();
                String _email = usr_email.getText().toString().trim();
                String _password = usr_pass.getText().toString().trim();

                if (TextUtils.isEmpty(_email) || TextUtils.isEmpty(_password)|| TextUtils.isEmpty(_fullName)) {
                    Toast.makeText(RegisterActivity.this, "All filed are required!", Toast.LENGTH_LONG).show();
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
                        Toast.makeText(RegisterActivity.this, "Please input valid email", Toast.LENGTH_LONG).show();
                    } else if (_password.length() < 8) {
                        Toast.makeText(RegisterActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        createUserAndSignIn(_email,_password,_fullName);
                    }
                }}
        });

    }

    private void createUserAndSignIn(String email, String password, String fullName) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    saveUserData(email,fullName);
                } else {
                    Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveUserData(String email, String fullName) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("id", mAuth.getCurrentUser().getUid());
        userMap.put("full_name", fullName);
        userMap.put("user_email", email);

        databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Register Success, Select your area", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(RegisterActivity.this,SelectLocationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void logIn(View view) {
        finish();
    }
}