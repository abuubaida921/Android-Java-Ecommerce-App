package com.abuubaida921.esupermarkt.activity;

import android.app.Activity;
import android.content.Intent;
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

import com.abuubaida921.esupermarkt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

public class LoginActivity extends Activity {

    EditText usr_email, usr_pass;
    Button login_btn;
    FirebaseAuth mAuth;
    TextView forgot_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        usr_email = findViewById(R.id.usr_email);
        usr_pass = findViewById(R.id.usr_pass);
        login_btn = findViewById(R.id.login_btn);
        forgot_pass = findViewById(R.id.forgot_pass);

        mAuth = FirebaseAuth.getInstance();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _email = usr_email.getText().toString().trim();
                String _password = usr_pass.getText().toString().trim();

                if (TextUtils.isEmpty(_email) || TextUtils.isEmpty(_password)) {
                    Toast.makeText(LoginActivity.this, "All filed are required!", Toast.LENGTH_LONG).show();
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
                        Toast.makeText(LoginActivity.this, "Please input valid email", Toast.LENGTH_LONG).show();
                    } else if (_password.length() < 8) {
                        Toast.makeText(LoginActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        login_btn.setVisibility(View.GONE);
                        UserSignIn(_email,_password);
                    }
                }
            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
    }

    private void UserSignIn(String mail,String pas) {
        mAuth.signInWithEmailAndPassword(mail, pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    login_btn.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Login error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signUp(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}