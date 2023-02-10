package com.abuubaida921.esupermarkt.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.ProductViewActivity;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ExclusiveOfferItemAdapter extends RecyclerView.Adapter<ExclusiveOfferItemAdapter.viewHolder> {


    Context context;
    ArrayList<ExclusiveOfferItemModel> arrayList;

    public ExclusiveOfferItemAdapter(Context context, ArrayList<ExclusiveOfferItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item_view_card, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {

        viewHolder.item_name.setText(arrayList.get(viewHolder.getAdapterPosition()).getName());
        viewHolder.item_unit.setText(arrayList.get(viewHolder.getAdapterPosition()).getUnit());
        viewHolder.item_price.setText("$"+arrayList.get(viewHolder.getAdapterPosition()).getUnit_price());

        if (arrayList.get(viewHolder.getAdapterPosition()).getImg_url().equals("default")) {
            viewHolder.item_image.setImageResource(R.drawable.ic_no_picture);
        } else {
            Glide.with(context).load(arrayList.get(viewHolder.getAdapterPosition()).getImg_url()).listener(new RequestListener<Drawable>() {
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
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, arrayList.get(viewHolder.getAdapterPosition()).getId()+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ProductViewActivity.class);
                intent.putExtra("id", arrayList.get(viewHolder.getAdapterPosition()).getId());
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        viewHolder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("product_id", arrayList.get(viewHolder.getAdapterPosition()).getId());
                map.put("added_unit", 1);
                map.put("unit_price", Double.parseDouble(arrayList.get(viewHolder.getAdapterPosition()).getUnit_price()));

                FirebaseDatabase.getInstance().getReference("cart").child(FirebaseAuth.getInstance().getUid()).child(arrayList.get(viewHolder.getAdapterPosition()).getId()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Successfully added to cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        TextView item_name, item_price, item_unit;
        CardView cardView;
        ImageView item_image,add_to_cart;

        public viewHolder(View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_img);
            add_to_cart = itemView.findViewById(R.id.add_to_cart);
            item_name = itemView.findViewById(R.id.txt_name);
            item_price = itemView.findViewById(R.id.txt_price);
            item_unit = itemView.findViewById(R.id.txt_unit);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}

