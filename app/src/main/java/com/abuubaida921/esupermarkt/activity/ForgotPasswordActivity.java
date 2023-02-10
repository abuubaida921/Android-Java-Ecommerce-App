package com.abuubaida921.esupermarkt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends Activity {

    Button pass_reset_btn;
    EditText usr_email;

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        usr_email=findViewById(R.id.usr_email);
        pass_reset_btn=findViewById(R.id.pass_reset_btn);

        pass_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _email = usr_email.getText().toString().trim();

                if (TextUtils.isEmpty(_email)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email filed is required!", Toast.LENGTH_LONG).show();
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Please input valid email", Toast.LENGTH_LONG).show();
                    } else {
                        sendPassResetMail(_email);
                    }
                }
            }
        });

    }

    private void sendPassResetMail(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email inbox or spam box & Please follow the instructions", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}