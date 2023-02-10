package com.abuubaida921.esupermarkt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.adapter.FaqItemAdapter;
import com.abuubaida921.esupermarkt.adapter.PaymentHistoryAdapter;
import com.abuubaida921.esupermarkt.model.FaqModel;
import com.abuubaida921.esupermarkt.model.PaymentHistoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PaymentHistoryActivity extends Activity {

    ProgressBar pay_history_progressBar;
    RecyclerView pay_history_recycler;
    ArrayList<PaymentHistoryModel> paymentHistoryModelArrayList = new ArrayList<>();
    PaymentHistoryAdapter paymentHistoryAdapter;
    String userID;

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
        setContentView(R.layout.activity_payment_history);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        userID = FirebaseAuth.getInstance().getUid();

        pay_history_progressBar = findViewById(R.id.pay_history_progressBar);
        pay_history_recycler = findViewById(R.id.pay_history_recycler);
        pay_history_recycler.setHasFixedSize(true);
        pay_history_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        readPaymentHistoryFromServer();
    }

    private void readPaymentHistoryFromServer() {
        FirebaseDatabase.getInstance().getReference("payments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pay_history_progressBar.setVisibility(View.VISIBLE);
                paymentHistoryModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PaymentHistoryModel paymentHistoryModel = dataSnapshot.getValue(PaymentHistoryModel.class);
                    if (paymentHistoryModel.getUser_id().equals(userID)) {
                        paymentHistoryModelArrayList.add(paymentHistoryModel);
                    }
                }
                paymentHistoryAdapter = new PaymentHistoryAdapter(getApplicationContext(), paymentHistoryModelArrayList);
                pay_history_recycler.setAdapter(paymentHistoryAdapter);
                pay_history_progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
