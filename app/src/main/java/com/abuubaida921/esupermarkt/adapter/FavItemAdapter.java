package com.abuubaida921.esupermarkt.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.ProductViewActivity;
import com.abuubaida921.esupermarkt.model.CartItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FavItemAdapter extends RecyclerView.Adapter<FavItemAdapter.viewHolder> {


    Context context;
    ArrayList<CartItemModel> arrayList;

    public FavItemAdapter(Context context, ArrayList<CartItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_item_view_card, viewGroup, false);
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
                double total_price = arrayList.get(viewHolder.getAdapterPosition()).getAdded_unit() * Double.parseDouble(exclusiveOfferItemModel.getUnit_price());
                viewHolder.txt_unit_total_price.setText("$ " + String.format("%.2f",total_price));

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductViewActivity.class);
                intent.putExtra("id", arrayList.get(viewHolder.getAdapterPosition()).getProduct_id());
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
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
        TextView item_name, txt_unit, txt_unit_total_price;
        CardView cardView;
        ImageView item_image;
        RelativeLayout relative_layout;

        public viewHolder(View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_img);
            item_name = itemView.findViewById(R.id.txt_name);
            txt_unit = itemView.findViewById(R.id.txt_unit);
            txt_unit_total_price = itemView.findViewById(R.id.txt_unit_total_price);
            relative_layout = itemView.findViewById(R.id.relative_layout);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}

