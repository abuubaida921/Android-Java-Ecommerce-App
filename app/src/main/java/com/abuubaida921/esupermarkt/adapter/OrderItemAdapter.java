package com.abuubaida921.esupermarkt.adapter;

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
import com.abuubaida921.esupermarkt.model.OrderItemModel;
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
import androidx.recyclerview.widget.RecyclerView;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.viewHolder> {

    Context context;
    ArrayList<OrderItemModel> arrayList;

    public OrderItemAdapter(Context context, ArrayList<OrderItemModel> OrderItemModel) {
        this.context = context;
        this.arrayList = OrderItemModel;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_view_card, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {

        int pos = viewHolder.getAdapterPosition();

        viewHolder.txt_order_id.setText("ID: "+arrayList.get(pos).getOrder_id());
        viewHolder.txt_order_address.setText(arrayList.get(pos).getDelivery_address());
        viewHolder.txt_order_total_price.setText("Total: $"+arrayList.get(pos).getOrder_total_amount());
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
        TextView txt_order_id, txt_order_address, txt_order_total_price;
        RelativeLayout relative_layout;

        public viewHolder(View itemView) {
            super(itemView);
            txt_order_id = itemView.findViewById(R.id.txt_order_id);
            txt_order_address = itemView.findViewById(R.id.txt_order_address);
            txt_order_total_price = itemView.findViewById(R.id.txt_order_total_price);
            relative_layout = itemView.findViewById(R.id.relative_layout);
        }

    }
}

