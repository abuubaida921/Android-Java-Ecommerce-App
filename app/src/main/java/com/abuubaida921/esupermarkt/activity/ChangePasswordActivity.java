package com.abuubaida921.esupermarkt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends Activity {
    Button update_pass_btn;
    EditText old_pass, new_pass, confirm_new_pass;

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
        setContentView(R.layout.activity_change_password);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        old_pass = findViewById(R.id.old_pass);
        new_pass = findViewById(R.id.new_pass);
        confirm_new_pass = findViewById(R.id.confirm_new_pass);
        update_pass_btn = findViewById(R.id.update_pass_btn);

        update_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _old_password = old_pass.getText().toString().trim();
                String _new_password = new_pass.getText().toString().trim();
                String _confirm_new_password = confirm_new_pass.getText().toString().trim();

                if (TextUtils.isEmpty(_old_password) || TextUtils.isEmpty(_new_password) || TextUtils.isEmpty(_confirm_new_password)) {
                    Toast.makeText(ChangePasswordActivity.this, "All filed are required", Toast.LENGTH_LONG).show();
                } else if (_old_password.length() < 8 || _new_password.length() < 8) {
                    Toast.makeText(ChangePasswordActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                } else if (!_new_password.equals(_confirm_new_password)) {
                    Toast.makeText(ChangePasswordActivity.this, "New password does not match!", Toast.LENGTH_SHORT).show();
                } else {


                    //get current user
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //before changing password re-authenticate the user
                    AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), _old_password);
                    user.reauthenticate(authCredential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //successfully authenticated, begin update

                                    user.updatePassword(_new_password)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //password updated
                                                    Toast.makeText(getApplicationContext(), "Password Updated...", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    //failed updating password, show reason
                                                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //authentication failed, show reason
                                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}