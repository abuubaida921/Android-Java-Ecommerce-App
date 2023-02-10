package com.abuubaida921.esupermarkt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.fragment.AccountFragment;
import com.abuubaida921.esupermarkt.fragment.CartFragment;
import com.abuubaida921.esupermarkt.fragment.ExploreFragment;
import com.abuubaida921.esupermarkt.fragment.FavouriteFragment;
import com.abuubaida921.esupermarkt.fragment.ShopFragment;
import com.abuubaida921.esupermarkt.model.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
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
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);
        bottomNavigationView.setSelectedItemId(R.id.shop);

        checkUserLocationToUpdate();
    }

    private void checkUserLocationToUpdate() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                if (userModel != null) {
                    if (userModel.getZone() == null || userModel.getArea() == null) {
                        startActivity(new Intent(MainActivity.this, SelectLocationActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    ShopFragment shopFragment = new ShopFragment();
    ExploreFragment exploreFragment = new ExploreFragment();
    CartFragment cartFragment = new CartFragment();
    FavouriteFragment favouriteFragment = new FavouriteFragment();
    AccountFragment accountFragment = new AccountFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.shop:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, shopFragment).commit();
                return true;

            case R.id.explore:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, exploreFragment).commit();
                return true;

            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, cartFragment).commit();
                return true;

            case R.id.favourite:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, favouriteFragment).commit();
                return true;

            case R.id.account:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();
                return true;
        }
        return false;
    }
}