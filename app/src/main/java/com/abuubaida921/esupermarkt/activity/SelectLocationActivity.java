package com.abuubaida921.esupermarkt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

public class SelectLocationActivity extends Activity{

    Button location_submit_btn;
    Spinner user_area,user_zone;
    String zone,area;
    FirebaseAuth mAuth;

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
        setContentView(R.layout.activity_select_location);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        location_submit_btn=findViewById(R.id.location_submit_btn);
        user_zone = findViewById(R.id.user_zone);
        user_area = findViewById(R.id.user_area);

        mAuth=FirebaseAuth.getInstance();

        // Spinner Drop down elements
        List<String> zones = new ArrayList<String>();
        zones.add("Bangladesh");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zones);
        user_zone.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        user_zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                ((TextView) user_zone.getSelectedView()).setTextColor(Color.BLACK);
                zone=item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> areas = new ArrayList<String>();
        areas.add("Barishal");
        areas.add("Chattogram");
        areas.add("Dhaka");
        areas.add("Khulna");
        areas.add("Rajshahi");
        areas.add("Rangpur");
        areas.add("Mymensingh");
        areas.add("Sylhet");

        ArrayAdapter<String> areasDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas);
        user_area.setAdapter(areasDataAdapter);
        areasDataAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        user_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                ((TextView) user_area.getSelectedView()).setTextColor(Color.BLACK);
                area=item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        location_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());

                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("zone", zone);
                userMap.put("area", area);

                databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SelectLocationActivity.this, "Location Updated", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(SelectLocationActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}