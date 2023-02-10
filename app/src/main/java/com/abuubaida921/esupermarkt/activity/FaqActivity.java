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
import com.abuubaida921.esupermarkt.adapter.CartItemAdapter;
import com.abuubaida921.esupermarkt.adapter.FaqItemAdapter;
import com.abuubaida921.esupermarkt.model.CartItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.abuubaida921.esupermarkt.model.FaqModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FaqActivity extends Activity {

    ProgressBar faq_progressBar;
    RecyclerView faq_recycler;
    ArrayList<FaqModel> faqModelArrayList = new ArrayList<>();
    FaqItemAdapter faqItemAdapter;

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
        setContentView(R.layout.activity_faq);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        faq_progressBar = findViewById(R.id.faq_progressBar);
        faq_recycler = findViewById(R.id.faq_recycler);
        faq_recycler.setHasFixedSize(true);
        faq_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        readFaqfromServer();
    }

    private void readFaqfromServer() {

        FirebaseDatabase.getInstance().getReference("faq").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                faq_progressBar.setVisibility(View.VISIBLE);
                faqModelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FaqModel faqModel = dataSnapshot.getValue(FaqModel.class);

                    faqModelArrayList.add(faqModel);
                }
                faqItemAdapter = new FaqItemAdapter(getApplicationContext(), faqModelArrayList);
                faq_recycler.setAdapter(faqItemAdapter);
                faq_progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}