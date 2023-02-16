package com.abuubaida921.esupermarkt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.adapter.ExclusiveOfferItemAdapter;
import com.abuubaida921.esupermarkt.model.CategoryItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.abuubaida921.esupermarkt.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CategoryWiseProductViewActivity extends Activity {

    RecyclerView category_wise_product_view_recycler;
    ProgressBar category_wise_product_view_progressBar;
    ArrayList<ExclusiveOfferItemModel> itemList = new ArrayList<>();
    ArrayList<ExclusiveOfferItemModel> exclusiveOfferList = new ArrayList<>();
    ArrayList<ExclusiveOfferItemModel> bestSellingList = new ArrayList<>();
    ExclusiveOfferItemAdapter bestSellingItemAdapter;
    String catId = null, type = null;
    TextView title_txt;
    ImageView btn_back, btn_share;
    LinearLayout search_layout;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
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
        setContentView(R.layout.activity_category_wise_product_view);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        catId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        search_layout = findViewById(R.id.search_layout);
        title_txt = findViewById(R.id.title_txt);
        btn_back = findViewById(R.id.btn_back);
        btn_share = findViewById(R.id.btn_share);
        category_wise_product_view_progressBar = findViewById(R.id.category_wise_product_view_progressBar);
        category_wise_product_view_recycler = findViewById(R.id.category_wise_product_view_recycler);
        category_wise_product_view_recycler.setHasFixedSize(true);
        category_wise_product_view_recycler.setLayoutManager(new GridLayoutManager(CategoryWiseProductViewActivity.this, 2));

        if (catId != null) readCategoryWiseProductItem(catId);
        else if (type != null) readTrendItem();

        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryWiseProductViewActivity.this, SearchActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CategoryWiseProductViewActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void readTrendItem() {
        title_txt.setText(getIntent().getStringExtra("title"));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                category_wise_product_view_progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExclusiveOfferItemModel exclusiveOfferItemModel = snapshot.getValue(ExclusiveOfferItemModel.class);
                    if (exclusiveOfferItemModel.getIs_exclusive()) {
                        exclusiveOfferList.add(exclusiveOfferItemModel);
                    }
                    if (exclusiveOfferItemModel.getIs_best_selling()) {
                        bestSellingList.add(exclusiveOfferItemModel);
                    }
                }

                if (type != null && type.equals("exclusive")) {
                    bestSellingItemAdapter = new ExclusiveOfferItemAdapter(getApplicationContext(), exclusiveOfferList);
                    category_wise_product_view_recycler.setAdapter(bestSellingItemAdapter);
                } else if (type != null && type.equals("best_selling")) {
                    bestSellingItemAdapter = new ExclusiveOfferItemAdapter(getApplicationContext(), bestSellingList);
                    category_wise_product_view_recycler.setAdapter(bestSellingItemAdapter);
                }
                category_wise_product_view_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readCategoryWiseProductItem(String catId) {


        DatabaseReference cat_reference = FirebaseDatabase.getInstance().getReference("categories").child(catId);

        cat_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryItemModel categoryItemModel = dataSnapshot.getValue(CategoryItemModel.class);
                title_txt.setText(categoryItemModel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference product_reference = FirebaseDatabase.getInstance().getReference("products");

        product_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                category_wise_product_view_progressBar.setVisibility(View.VISIBLE);
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExclusiveOfferItemModel exclusiveOfferItemModel = snapshot.getValue(ExclusiveOfferItemModel.class);
                    assert exclusiveOfferItemModel != null;
                    if (exclusiveOfferItemModel.getCat_id() != null && exclusiveOfferItemModel.getCat_id().equals(catId)) {
                        itemList.add(exclusiveOfferItemModel);
                    }
                }
                bestSellingItemAdapter = new ExclusiveOfferItemAdapter(getApplicationContext(), itemList);
                category_wise_product_view_recycler.setAdapter(bestSellingItemAdapter);
                category_wise_product_view_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}