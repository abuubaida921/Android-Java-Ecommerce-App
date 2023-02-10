package com.abuubaida921.esupermarkt.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.model.CartItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.abuubaida921.esupermarkt.model.PaymentMethodListModel;
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
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentMethodListItemAdapter extends RecyclerView.Adapter<PaymentMethodListItemAdapter.viewHolder> {

    Context context;
    ArrayList<PaymentMethodListModel> arrayList;
    TextView selected_method;
    AlertDialog alertDialog;
    ImageView imageView;

    public PaymentMethodListItemAdapter(Context context, ArrayList<PaymentMethodListModel> arrayList, AlertDialog alertDialog,TextView selected_method,ImageView imageView) {
        this.context = context;
        this.arrayList = arrayList;
        this.alertDialog = alertDialog;
        this.selected_method = selected_method;
        this.imageView = imageView;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_method_item_select_layout, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {

        Glide.with(context).load(arrayList.get(viewHolder.getAdapterPosition()).getImgUrl()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(viewHolder.img);
        viewHolder.name.setText(arrayList.get(viewHolder.getAdapterPosition()).getName());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_method.setText(arrayList.get(viewHolder.getAdapterPosition()).getId());
                Glide.with(context).load(arrayList.get(viewHolder.getAdapterPosition()).getImgUrl()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(imageView);
                alertDialog.dismiss();
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
        TextView name;
        ImageView img;
        CardView cardView;

        public viewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}

