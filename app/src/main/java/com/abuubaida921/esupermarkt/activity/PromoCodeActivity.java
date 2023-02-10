package com.abuubaida921.esupermarkt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.abuubaida921.esupermarkt.adapter.FaqItemAdapter;
import com.abuubaida921.esupermarkt.adapter.PromoCodeItemAdapter;
import com.abuubaida921.esupermarkt.model.FaqModel;
import com.abuubaida921.esupermarkt.model.PromoCodeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PromoCodeActivity extends Activity {
    ProgressBar promo_progressBar;
    RecyclerView promo_recycler;
    ArrayList<PromoCodeModel> promoCodeModelArrayList = new ArrayList<>();
    PromoCodeItemAdapter promoCodeItemAdapter;

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
        setContentView(R.layout.activity_promo_code);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        promo_progressBar=findViewById(R.id.promo_progressBar);
        promo_recycler=findViewById(R.id.promo_recycler);
        promo_recycler.setHasFixedSize(true);
        promo_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        readPromoCodesFromServer();


    }

    private void readPromoCodesFromServer() {

        FirebaseDatabase.getInstance().getReference("promo_codes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promo_progressBar.setVisibility(View.VISIBLE);
                promoCodeModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PromoCodeModel promoCodeModel = dataSnapshot.getValue(PromoCodeModel.class);

                    promoCodeModelArrayList.add(promoCodeModel);
                }
                promoCodeItemAdapter = new PromoCodeItemAdapter(getApplicationContext(), promoCodeModelArrayList);
                promo_recycler.setAdapter(promoCodeItemAdapter);
                promo_progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}