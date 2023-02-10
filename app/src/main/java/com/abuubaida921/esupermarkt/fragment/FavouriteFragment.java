package com.abuubaida921.esupermarkt.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.adapter.CartItemAdapter;
import com.abuubaida921.esupermarkt.adapter.FavItemAdapter;
import com.abuubaida921.esupermarkt.model.CartItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class FavouriteFragment extends Fragment {

    ProgressBar fav_progressBar;
    RecyclerView fav_recycler;
    String userID;
    ArrayList<CartItemModel> favList = new ArrayList<>();

    FavItemAdapter favItemAdapter;
    Button add_all_to_cart_btn;
    ArrayList<CartItemModel> cartList = new ArrayList<>();
    ArrayList<CartItemModel> tempList = new ArrayList<>();

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        add_all_to_cart_btn = view.findViewById(R.id.add_all_to_cart_btn);
        fav_progressBar = view.findViewById(R.id.fav_progressBar);
        fav_recycler = view.findViewById(R.id.fav_recycler);
        fav_recycler.setHasFixedSize(true);
        fav_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        userID = FirebaseAuth.getInstance().getUid();

        add_all_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        readFavItem();
        return view;
    }

    private void readFavItem() {
        FirebaseDatabase.getInstance().getReference("favourite").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fav_progressBar.setVisibility(View.VISIBLE);
                favList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItemModel cartItemModel = dataSnapshot.getValue(CartItemModel.class);

                    favList.add(cartItemModel);

                }
                favItemAdapter = new FavItemAdapter(getActivity(), favList);
                fav_recycler.setAdapter(favItemAdapter);
                fav_progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}