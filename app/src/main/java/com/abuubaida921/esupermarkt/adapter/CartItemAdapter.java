package com.abuubaida921.esupermarkt.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.CategoryWiseProductViewActivity;
import com.abuubaida921.esupermarkt.activity.ProductViewActivity;
import com.abuubaida921.esupermarkt.model.CartItemModel;
import com.abuubaida921.esupermarkt.model.CategoryItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.viewHolder> {


    Context context;
    ArrayList<CartItemModel> arrayList;
    String userID;

    public CartItemAdapter(Context context, ArrayList<CartItemModel> arrayList, String userID) {
        this.context = context;
        this.arrayList = arrayList;
        this.userID = userID;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_view_card, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {

        FirebaseDatabase.getInstance().getReference("products").child(arrayList.get(viewHolder.getAdapterPosition()).getProduct_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ExclusiveOfferItemModel exclusiveOfferItemModel = snapshot.getValue(ExclusiveOfferItemModel.class);
                viewHolder.item_name.setText(exclusiveOfferItemModel.getName());
                viewHolder.txt_unit.setText(exclusiveOfferItemModel.getUnit());
                if (arrayList.get(viewHolder.getAdapterPosition()).getAdded_unit() > 1) {
                    viewHolder.reduce.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.reduce.setVisibility(View.GONE);
                }
                viewHolder.singleItemSelectedUnit.setText("" + arrayList.get(viewHolder.getAdapterPosition()).getAdded_unit());
                double total_price = arrayList.get(viewHolder.getAdapterPosition()).getAdded_unit() * Double.parseDouble(exclusiveOfferItemModel.getUnit_price());
                viewHolder.txt_unit_total_price.setText("$ " + String.format("%.2f", total_price));

                if (exclusiveOfferItemModel.getImg_url().equals("default")) {
                    viewHolder.item_image.setImageResource(R.drawable.ic_no_picture);
                } else {
                    Glide.with(context).load(exclusiveOfferItemModel.getImg_url()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(viewHolder.item_image);
                }

                viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.reduce.setVisibility(View.GONE);
                        viewHolder.progress_reduce.setVisibility(View.VISIBLE);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("added_unit", arrayList.get(viewHolder.getAdapterPosition()).getAdded_unit() - 1);
                        map.put("unit_price", Double.parseDouble(exclusiveOfferItemModel.getUnit_price()));
                        FirebaseDatabase.getInstance().getReference("cart").child(userID).child(arrayList.get(viewHolder.getAdapterPosition()).getProduct_id()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (arrayList.get(viewHolder.getAdapterPosition()).getAdded_unit() > 1) {
                                    viewHolder.progress_reduce.setVisibility(View.GONE);
                                    viewHolder.reduce.setVisibility(View.VISIBLE);
                                } else {
                                    viewHolder.reduce.setVisibility(View.GONE);
                                    viewHolder.progress_reduce.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
                viewHolder.increase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.increase.setVisibility(View.GONE);
                        viewHolder.progress_increase.setVisibility(View.VISIBLE);
                        try {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("added_unit", arrayList.get(viewHolder.getAdapterPosition()).getAdded_unit() + 1);
                            map.put("unit_price", Double.parseDouble(exclusiveOfferItemModel.getUnit_price()));
                            FirebaseDatabase.getInstance().getReference("cart").child(userID).child(arrayList.get(viewHolder.getAdapterPosition()).getProduct_id()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        viewHolder.progress_increase.setVisibility(View.GONE);
                                        viewHolder.increase.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something wrong");
                        }
                    }
                });
                viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("cart").child(userID).child(arrayList.get(viewHolder.getAdapterPosition()).getProduct_id()).removeValue();
                    }
                });
                viewHolder.relative_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ProductViewActivity.class);
                        intent.putExtra("id", arrayList.get(viewHolder.getAdapterPosition()).getProduct_id());
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView item_name, txt_unit, singleItemSelectedUnit, txt_unit_total_price;
        ImageView item_image, reduce, increase, remove;
        RelativeLayout relative_layout;
        ProgressBar progress_reduce, progress_increase;

        public viewHolder(View itemView) {
            super(itemView);
            progress_reduce = itemView.findViewById(R.id.progress_reduce);
            progress_increase = itemView.findViewById(R.id.progress_increase);
            reduce = itemView.findViewById(R.id.reduce);
            increase = itemView.findViewById(R.id.increase);
            remove = itemView.findViewById(R.id.remove);
            item_image = itemView.findViewById(R.id.item_img);
            item_name = itemView.findViewById(R.id.txt_name);
            txt_unit = itemView.findViewById(R.id.txt_unit);
            txt_unit_total_price = itemView.findViewById(R.id.txt_unit_total_price);
            singleItemSelectedUnit = itemView.findViewById(R.id.singleItemSelectedUnit);
            relative_layout = itemView.findViewById(R.id.relative_layout);
        }

    }
}

