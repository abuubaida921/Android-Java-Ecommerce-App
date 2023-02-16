package com.abuubaida921.esupermarkt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.adapter.CategoryItemAdapter;
import com.abuubaida921.esupermarkt.adapter.ExclusiveOfferItemAdapter;
import com.abuubaida921.esupermarkt.adapter.GridListAdapter;
import com.abuubaida921.esupermarkt.model.CategoryItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends Activity {

    ProgressBar search_progressBar;
    RecyclerView search_recycler;
    EditText search_view;
    ArrayList<ExclusiveOfferItemModel> itemList = new ArrayList<>();
    ArrayList<ExclusiveOfferItemModel> filtereditemList = new ArrayList<>();
    ArrayList<ExclusiveOfferItemModel> tempitemList = new ArrayList<>();
    BottomSheetDialog bottomSheetDialog;
    private ArrayList<String> arrayList;
    private GridListAdapter adapter;
    ExclusiveOfferItemAdapter itemAdapter;
    ImageView clear_search_box, filter_btn;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    ArrayList<CategoryItemModel> categoryList = new ArrayList<>();


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
        setContentView(R.layout.activity_search);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        clear_search_box = findViewById(R.id.clear_search_box);
        filter_btn = findViewById(R.id.filter_btn);
        search_progressBar = findViewById(R.id.search_progressBar);
        search_recycler = findViewById(R.id.search_recycler);
        search_recycler.setHasFixedSize(true);
        search_recycler.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));

        search_view = findViewById(R.id.search_view);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("categories");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CategoryItemModel categoryItemModel = snapshot.getValue(CategoryItemModel.class);

                    categoryList.add(categoryItemModel);
                }
                listItems = new String[categoryList.size()];
                for (int j = 0; j < categoryList.size(); j++) {
                    listItems[j] = categoryList.get(j).getName();
                }
                checkedItems = new boolean[listItems.length];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        loadDataFromServer();

        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopup();
            }
        });

        clear_search_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_view.setText("");
                clear_search_box.setVisibility(View.GONE);
            }
        });

        search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    searchFromList(s.toString());
                    clear_search_box.setVisibility(View.VISIBLE);
                    filter_btn.setVisibility(View.VISIBLE);
                } else {
                    search_recycler.setAdapter(null);
                    clear_search_box.setVisibility(View.GONE);
                    filter_btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showFilterPopup() {

        itemAdapter = new ExclusiveOfferItemAdapter(getApplicationContext(), filtereditemList);
        search_recycler.setAdapter(itemAdapter);

        bottomSheetDialog = new BottomSheetDialog(SearchActivity.this, R.style.BottomThemeDialogueTheme);
        View bottomSheetView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_filter_bottom_sheet_layout, findViewById(R.id.checkout_bottom_sheet_layout));
        Button submit_btn = bottomSheetView.findViewById(R.id.submit_btn);
        ListView listView = bottomSheetView.findViewById(R.id.list_view);
        arrayList = new ArrayList<>();
        for (int i = 1; i <= 5; i++)
            arrayList.add("ListView Items " + i);//Adding items to recycler view

        adapter = new GridListAdapter(getApplicationContext(), categoryList);
        listView.setAdapter(adapter);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempitemList.clear();
                SparseBooleanArray selectedRows = adapter.getSelectedIds();
                if (selectedRows.size() > 0) {
                    for (int i = 0; i < selectedRows.size(); i++) {
                        if (selectedRows.valueAt(i)) {
                            for (ExclusiveOfferItemModel exclusiveOfferItemModel : filtereditemList) {
                                if (exclusiveOfferItemModel.getCat_id().equals(categoryList.get(selectedRows.keyAt(i)).getId())) {
                                    tempitemList.add(exclusiveOfferItemModel);
                                }
                            }
                        }
                    }
                    itemAdapter = new ExclusiveOfferItemAdapter(getApplicationContext(), tempitemList);
                    search_recycler.setAdapter(itemAdapter);
                } else {
                    itemAdapter = new ExclusiveOfferItemAdapter(getApplicationContext(), filtereditemList);
                    search_recycler.setAdapter(itemAdapter);
                }
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void loadDataFromServer() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExclusiveOfferItemModel exclusiveOfferItemModel = snapshot.getValue(ExclusiveOfferItemModel.class);
                    itemList.add(exclusiveOfferItemModel);
                }
                search_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchFromList(String query) {
        search_progressBar.setVisibility(View.VISIBLE);
        filtereditemList.clear();
        for (ExclusiveOfferItemModel exclusiveOfferItemModel : itemList) {
            if (exclusiveOfferItemModel.getName().toLowerCase().contains(query.toLowerCase())) {
                filtereditemList.add(exclusiveOfferItemModel);
            }
        }
        itemAdapter = new ExclusiveOfferItemAdapter(getApplicationContext(), filtereditemList);
        search_recycler.setAdapter(itemAdapter);
        search_progressBar.setVisibility(View.GONE);
    }
}