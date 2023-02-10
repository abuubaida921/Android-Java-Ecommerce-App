package com.abuubaida921.esupermarkt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.adapter.DeliveryAddressItemAdapter;
import com.abuubaida921.esupermarkt.adapter.OrderItemAdapter;
import com.abuubaida921.esupermarkt.model.DeliveryAddressModel;
import com.abuubaida921.esupermarkt.model.OrderItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryAddressesActivity extends Activity {

    ProgressBar delivery_address_progressBar;
    RecyclerView delivery_address_recycler;
    String userID;
    ArrayList<DeliveryAddressModel> deliveryAddressModelArrayList = new ArrayList<>();

    DeliveryAddressItemAdapter deliveryAddressItemAdapter;
    Button add_address_btn;

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
        setContentView(R.layout.activity_delivery_addresses);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        add_address_btn=findViewById(R.id.add_address_btn);
        delivery_address_progressBar=findViewById(R.id.delivery_address_progressBar);
        delivery_address_recycler=findViewById(R.id.delivery_address_recycler);
        delivery_address_recycler.setHasFixedSize(true);
        delivery_address_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        userID = FirebaseAuth.getInstance().getUid();
        DatabaseReference deliveryAddressRef = FirebaseDatabase.getInstance().getReference("delivery_address");

        add_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DeliveryAddressesActivity.this, R.style.BottomThemeDialogueTheme);
                View bottomSheetView = LayoutInflater.from(DeliveryAddressesActivity.this).inflate(R.layout.address_bottom_sheet_layout, findViewById(R.id.checkout_bottom_sheet_layout));
                EditText edit_address = bottomSheetView.findViewById(R.id.edit_address);
                Button add_d_address_btn = bottomSheetView.findViewById(R.id.add_d_address_btn);

                add_d_address_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        long key = System.currentTimeMillis();

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id", key+"");
                        map.put("delivery_address", edit_address.getText().toString());
                        map.put("user_id", userID);

                        deliveryAddressRef.child(key+"").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Address Added", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        });

                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        deliveryAddressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                delivery_address_progressBar.setVisibility(View.VISIBLE);
                deliveryAddressModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DeliveryAddressModel deliveryAddressModel = dataSnapshot.getValue(DeliveryAddressModel.class);
                    if (deliveryAddressModel.getUser_id() != null && deliveryAddressModel.getUser_id().equals(userID)) {
                        deliveryAddressModelArrayList.add(deliveryAddressModel);
                    }

                    deliveryAddressItemAdapter = new DeliveryAddressItemAdapter(DeliveryAddressesActivity.this, deliveryAddressModelArrayList);
                    delivery_address_recycler.setAdapter(deliveryAddressItemAdapter);
                }
                delivery_address_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}