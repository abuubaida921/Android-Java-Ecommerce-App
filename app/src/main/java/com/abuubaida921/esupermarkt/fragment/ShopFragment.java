package com.abuubaida921.esupermarkt.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.MainActivity;
import com.abuubaida921.esupermarkt.activity.SelectLocationActivity;
import com.abuubaida921.esupermarkt.adapter.ExclusiveOfferItemAdapter;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.abuubaida921.esupermarkt.model.UserModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ShopFragment extends Fragment {

    FirebaseAuth mAuth;
    TextView user_location;
    RecyclerView exclusive_recycler, best_selling_recycler;
    ProgressBar exclusive_progressBar, best_selling_progressBar;
    ArrayList<ExclusiveOfferItemModel> exclusiveOfferList = new ArrayList<>();
    ArrayList<ExclusiveOfferItemModel> bestSellingList = new ArrayList<>();
    ExclusiveOfferItemAdapter exclusiveOfferItemAdapter;
    ExclusiveOfferItemAdapter bestSellingItemAdapter;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        user_location = view.findViewById(R.id.user_location);
        best_selling_progressBar = view.findViewById(R.id.best_selling_progressBar);
        exclusive_progressBar = view.findViewById(R.id.exclusive_progressBar);
        exclusive_recycler = view.findViewById(R.id.exclusive_recycler);
        exclusive_recycler.setHasFixedSize(true);
        exclusive_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        best_selling_recycler = view.findViewById(R.id.best_selling_recycler);
        best_selling_recycler.setHasFixedSize(true);
        best_selling_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAuth = FirebaseAuth.getInstance();
        readExclusiveItem();
        return view;
    }

    private void readExclusiveItem() {

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("products");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                if (userModel != null) {
                    if (userModel.getZone() == null || userModel.getArea() == null) {
                        user_location.setText("Please Select Location");
                    } else {
                        user_location.setText(userModel.getZone() + ", " + userModel.getArea());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exclusive_progressBar.setVisibility(View.VISIBLE);
                best_selling_progressBar.setVisibility(View.VISIBLE);
                exclusiveOfferList.clear();
                bestSellingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExclusiveOfferItemModel exclusiveOfferItemModel = snapshot.getValue(ExclusiveOfferItemModel.class);
                    if (exclusiveOfferItemModel.getIs_exclusive()) {
                        exclusiveOfferList.add(exclusiveOfferItemModel);
                    }
                    if (exclusiveOfferItemModel.getIs_best_selling()) {
                        bestSellingList.add(exclusiveOfferItemModel);
                    }
                }
                exclusiveOfferItemAdapter = new ExclusiveOfferItemAdapter(getContext(), exclusiveOfferList);
                exclusive_recycler.setAdapter(exclusiveOfferItemAdapter);
                exclusive_progressBar.setVisibility(View.GONE);
                bestSellingItemAdapter = new ExclusiveOfferItemAdapter(getContext(), bestSellingList);
                best_selling_recycler.setAdapter(bestSellingItemAdapter);
                best_selling_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}