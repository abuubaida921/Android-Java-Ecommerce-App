package com.abuubaida921.esupermarkt.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.adapter.CategoryItemAdapter;
import com.abuubaida921.esupermarkt.adapter.ExclusiveOfferItemAdapter;
import com.abuubaida921.esupermarkt.model.CategoryItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    RecyclerView category_recycler;
    ProgressBar category_progressBar;
    ArrayList<CategoryItemModel> categoryList = new ArrayList<>();
    CategoryItemAdapter categoryItemAdapter;

    public ExploreFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_explore, container, false);
        category_progressBar = view.findViewById(R.id.category_progressBar);
        category_recycler = view.findViewById(R.id.category_recycler);
        category_recycler.setHasFixedSize(true);
        category_recycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        readCategoryItem();
        return view;
    }

    private void readCategoryItem() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("categories");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                category_progressBar.setVisibility(View.VISIBLE);
                categoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CategoryItemModel categoryItemModel = snapshot.getValue(CategoryItemModel.class);

                    categoryList.add(categoryItemModel);
                }
                categoryItemAdapter = new CategoryItemAdapter(getContext(), categoryList);
                category_recycler.setAdapter(categoryItemAdapter);
                category_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}