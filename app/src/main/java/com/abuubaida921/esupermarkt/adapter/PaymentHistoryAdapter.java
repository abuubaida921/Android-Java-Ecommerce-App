package com.abuubaida921.esupermarkt.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.Config;
import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.model.PaymentHistoryModel;
import com.abuubaida921.esupermarkt.model.PaymentMethodListModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.viewHolder> {

    Context context;
    ArrayList<PaymentHistoryModel> arrayList;

    public PaymentHistoryAdapter(Context context, ArrayList<PaymentHistoryModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_history_item_layout, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {

        int pos = viewHolder.getAdapterPosition();
        String img_url="";

        if (arrayList.get(pos).getTxn_id().startsWith("pi")) {
            img_url= Config.stripeIconUrl;
        } else if (arrayList.get(pos).getTxn_id().startsWith("pay")) {
            img_url= Config.razorPayIconUrl;
        } else if (arrayList.get(pos).getTxn_id().startsWith("cod")) {
            img_url= Config.codIconUrl;
        }

        Glide.with(context).load(img_url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(viewHolder.img);

        viewHolder.order_id.setText(arrayList.get(pos).getOrder_id());
//        DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        DateFormat obj = new SimpleDateFormat("dd MMM yyyy");
        Date res = new Date(arrayList.get(pos).getPaid_at());
        viewHolder.payment_date.setText(obj.format(res));
        viewHolder.amount.setText(arrayList.get(pos).getAmount());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked " + pos, Toast.LENGTH_SHORT).show();
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
        TextView order_id, payment_date, amount;
        ImageView img;
        CardView cardView;

        public viewHolder(View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            payment_date = itemView.findViewById(R.id.payment_date);
            amount = itemView.findViewById(R.id.amount);
            img = itemView.findViewById(R.id.img);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}

