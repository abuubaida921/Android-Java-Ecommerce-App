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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.CategoryWiseProductViewActivity;
import com.abuubaida921.esupermarkt.activity.MainActivity;
import com.abuubaida921.esupermarkt.activity.SearchActivity;
import com.abuubaida921.esupermarkt.activity.SelectLocationActivity;
import com.abuubaida921.esupermarkt.adapter.ExclusiveOfferItemAdapter;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.abuubaida921.esupermarkt.model.UserModel;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class ShopFragment extends Fragment {

    FirebaseAuth mAuth;
    TextView user_location,exclusive_see_all,best_selling_all;
    RecyclerView exclusive_recycler, best_selling_recycler;
    ProgressBar exclusive_progressBar, best_selling_progressBar;
    ArrayList<SlideModel> slideModelArrayList  = new ArrayList<>();
    ImageSlider imageSlider;
    ArrayList<ExclusiveOfferItemModel> exclusiveOfferList = new ArrayList<>();
    ArrayList<ExclusiveOfferItemModel> bestSellingList = new ArrayList<>();
    ExclusiveOfferItemAdapter exclusiveOfferItemAdapter;
    ExclusiveOfferItemAdapter bestSellingItemAdapter;
    LinearLayout search_layout;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        search_layout = view.findViewById(R.id.search_layout);
        imageSlider = view.findViewById(R.id.image_slider);
        user_location = view.findViewById(R.id.user_location);
        exclusive_see_all = view.findViewById(R.id.exclusive_see_all);
        best_selling_all = view.findViewById(R.id.best_selling_all);
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
        slideModelArrayList.clear();
        slideModelArrayList.add(new SlideModel("https://images.unsplash.com/photo-1581515286348-98549702050f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=924&q=70",ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel("https://images.unsplash.com/photo-1506617420156-8e4536971650?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=924&q=70",ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel("https://images.unsplash.com/photo-1516594798947-e65505dbb29d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=924&q=70",ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel("https://images.unsplash.com/photo-1506617564039-2f3b650b7010?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=924&q=70",ScaleTypes.FIT));
        imageSlider.setImageList(slideModelArrayList);

        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });
        exclusive_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryWiseProductViewActivity.class);
                intent.putExtra("type", "exclusive");
                intent.putExtra("title", "Exclusive Offer");
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });
        best_selling_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryWiseProductViewActivity.class);
                intent.putExtra("type", "best_selling");
                intent.putExtra("title", "Best Selling");
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });
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
                        user_location.setText( userModel.getArea()+ ", " + userModel.getZone());
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