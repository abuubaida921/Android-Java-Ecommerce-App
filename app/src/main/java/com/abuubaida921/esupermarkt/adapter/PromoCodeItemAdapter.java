package com.abuubaida921.esupermarkt.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.model.FaqModel;
import com.abuubaida921.esupermarkt.model.PromoCodeModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PromoCodeItemAdapter extends RecyclerView.Adapter<PromoCodeItemAdapter.viewHolder> {

    Context context;
    ArrayList<PromoCodeModel> arrayList;
    DateFormat dateFormat= new SimpleDateFormat("dd MMM yyyy");

    public PromoCodeItemAdapter(Context context, ArrayList<PromoCodeModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.promo_code_item_layout, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {
        viewHolder.code.setText(arrayList.get(viewHolder.getAdapterPosition()).getCode());
        viewHolder.discount.setText((arrayList.get(viewHolder.getAdapterPosition()).getDiscount() * 100) + "% off");
        viewHolder.description.setText(arrayList.get(viewHolder.getAdapterPosition()).getDescription());
        viewHolder.start_from.setText("From: "+dateFormat.format(new Date(arrayList.get(viewHolder.getAdapterPosition()).getValid_from())));
        viewHolder.end_at.setText("Till: "+dateFormat.format(new Date(arrayList.get(viewHolder.getAdapterPosition()).getValid_till())));

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Promo Code", arrayList.get(viewHolder.getAdapterPosition()).getCode());
                manager.setPrimaryClip(clipData);
                Toast.makeText(context, "Promo Code Copied.", Toast.LENGTH_SHORT).show();

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
        TextView code, discount, description, start_from, end_at;
        CardView cardView;

        public viewHolder(View itemView) {
            super(itemView);
            discount = itemView.findViewById(R.id.discount);
            description = itemView.findViewById(R.id.description);
            code = itemView.findViewById(R.id.code);
            start_from = itemView.findViewById(R.id.start_from);
            end_at = itemView.findViewById(R.id.end_at);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}

