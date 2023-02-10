package com.abuubaida921.esupermarkt.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.model.PromoCodeModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PromoCodeItemSelectAdapter extends RecyclerView.Adapter<PromoCodeItemSelectAdapter.viewHolder> {

    Context context;
    ArrayList<PromoCodeModel> arrayList;
    DateFormat dateFormat= new SimpleDateFormat("dd MMM yyyy");
    AlertDialog alertDialog;
    TextView selected_coupon,tv;
    Double total_price;

    public PromoCodeItemSelectAdapter(Context context, ArrayList<PromoCodeModel> arrayList, AlertDialog alertDialog, TextView selected_coupon,Double total_price,TextView tv) {
        this.context = context;
        this.arrayList = arrayList;
        this.alertDialog = alertDialog;
        this.selected_coupon = selected_coupon;
        this.tv = tv;
        this.total_price = total_price;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.promo_code_item_select_layout, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {
        viewHolder.code.setText(arrayList.get(viewHolder.getAdapterPosition()).getCode());
        viewHolder.discount.setText((arrayList.get(viewHolder.getAdapterPosition()).getDiscount() * 100) + "% off");

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_coupon.setText(arrayList.get(viewHolder.getAdapterPosition()).getCode());
                double final_total_price=(1-arrayList.get(viewHolder.getAdapterPosition()).getDiscount())*total_price;
                tv.setText(String.format("%.2f", final_total_price));
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
        TextView code, discount;
        CardView cardView;

        public viewHolder(View itemView) {
            super(itemView);
            discount = itemView.findViewById(R.id.discount);
            code = itemView.findViewById(R.id.code);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}

