package com.abuubaida921.esupermarkt.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.model.DeliveryAddressModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryAddressItemSelectAdapter extends RecyclerView.Adapter<DeliveryAddressItemSelectAdapter.viewHolder> {

    Context context;
    ArrayList<DeliveryAddressModel> arrayList;
    AlertDialog alertDialog;
    TextView selected_address;

    public DeliveryAddressItemSelectAdapter(Context context, ArrayList<DeliveryAddressModel> OrderItemModel, AlertDialog alertDialog,TextView selected_address) {
        this.context = context;
        this.arrayList = OrderItemModel;
        this.alertDialog = alertDialog;
        this.selected_address = selected_address;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_address_item_view_card, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {

        int pos = viewHolder.getAdapterPosition();

        viewHolder.txt_order_id.setText(arrayList.get(pos).getId());
        viewHolder.txt_order_address.setText(arrayList.get(pos).getDelivery_address());
        viewHolder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_address.setText(arrayList.get(pos).getDelivery_address());
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

