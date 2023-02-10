package com.abuubaida921.esupermarkt.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.adapter.OrderItemAdapter;
import com.abuubaida921.esupermarkt.model.OrderItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllOrderActivity extends Activity {

    ProgressBar order_history_progressBar;
    RecyclerView order_history_recycler;
    String userID;
    ArrayList<OrderItemModel> orderItemModelArrayList = new ArrayList<>();

    OrderItemAdapter orderItemAdapter;

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
        setContentView(R.layout.activity_all_order);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        order_history_progressBar=findViewById(R.id.order_history_progressBar);
        order_history_recycler=findViewById(R.id.order_history_recycler);
        order_history_recycler.setHasFixedSize(true);
        order_history_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//        userID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        userID = FirebaseAuth.getInstance().getUid();

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders");
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                order_history_progressBar.setVisibility(View.VISIBLE);
                orderItemModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderItemModel orderItemModel = dataSnapshot.getValue(OrderItemModel.class);
                    if (orderItemModel.getUser_id() != null && orderItemModel.getUser_id().equals(userID)) {
                        orderItemModelArrayList.add(orderItemModel);
                    }

                    orderItemAdapter = new OrderItemAdapter(getApplicationContext(), orderItemModelArrayList);
                    order_history_recycler.setAdapter(orderItemAdapter);
                }
                order_history_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}