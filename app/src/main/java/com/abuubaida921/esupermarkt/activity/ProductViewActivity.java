package com.abuubaida921.esupermarkt.activity;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.model.CartItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProductViewActivity extends Activity {

    String productId, singleUnitPrice;
    ImageView item_img, fav_img_view, remove_fav_img_view, btn_back, btn_share, reduce, increase;
    Button add_to_cart_btn;
    DatabaseReference databaseReference, fav_databaseReference;
    int alreadyAddedItemNo = 0, userAddedItemNo = 1;
    String userID;
    TextView txt_name, txt_unit, txt_price, singleItemSelectedUnit;

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
        setContentView(R.layout.activity_product_view);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        productId = getIntent().getStringExtra("id");
        reduce = findViewById(R.id.reduce);
        increase = findViewById(R.id.increase);
        singleItemSelectedUnit = findViewById(R.id.singleItemSelectedUnit);
        txt_name = findViewById(R.id.txt_name);
        txt_unit = findViewById(R.id.txt_unit);
        txt_price = findViewById(R.id.txt_price);
        btn_back = findViewById(R.id.btn_back);
        btn_share = findViewById(R.id.btn_share);
        item_img = findViewById(R.id.item_img);
        fav_img_view = findViewById(R.id.fav_img_view);
        remove_fav_img_view = findViewById(R.id.remove_fav_img_view);
        add_to_cart_btn = findViewById(R.id.add_to_cart_btn);

        userID = FirebaseAuth.getInstance().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("cart").child(userID);
        fav_databaseReference = FirebaseDatabase.getInstance().getReference("favourite").child(userID);

        databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    alreadyAddedItemNo = snapshot.getValue(CartItemModel.class).getAdded_unit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fav_databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    fav_img_view.setVisibility(View.GONE);
                    remove_fav_img_view.setVisibility(View.VISIBLE);
                } else {
                    remove_fav_img_view.setVisibility(View.GONE);
                    fav_img_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("products").child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ExclusiveOfferItemModel exclusiveOfferItemModel = snapshot.getValue(ExclusiveOfferItemModel.class);
                singleUnitPrice = exclusiveOfferItemModel.getUnit_price();
                if (exclusiveOfferItemModel.getImg_url() != null && exclusiveOfferItemModel.getImg_url().equals("default")) {
                    item_img.setImageResource(R.drawable.ic_no_picture);
                } else {
                    Glide.with(getApplicationContext()).load(exclusiveOfferItemModel.getImg_url()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(item_img);
                }
                txt_price.setText("$" + singleUnitPrice);
                txt_name.setText(exclusiveOfferItemModel.getName());
                txt_unit.setText(exclusiveOfferItemModel.getUnit());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAddedItemNo > 1) {
                    userAddedItemNo -= 1;
                    singleItemSelectedUnit.setText(String.valueOf(userAddedItemNo));
                }
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAddedItemNo += 1;
                singleItemSelectedUnit.setText(String.valueOf(userAddedItemNo));
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
                Toast.makeText(ProductViewActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("product_id", productId);
                map.put("added_unit", alreadyAddedItemNo + userAddedItemNo);
                map.put("unit_price", Double.parseDouble(singleUnitPrice));

                databaseReference.child(productId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProductViewActivity.this, "Successfully added to cart", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

        fav_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("product_id", productId);
                map.put("added_unit", 1);
                map.put("unit_price", Double.parseDouble(singleUnitPrice));

                fav_databaseReference.child(productId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProductViewActivity.this, "Successfully added to favourite", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        remove_fav_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fav_databaseReference.child(productId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProductViewActivity.this, "Successfully removed from favourite", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}