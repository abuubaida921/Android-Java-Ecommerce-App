package com.abuubaida921.esupermarkt.adapter;

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
import com.abuubaida921.esupermarkt.activity.DeliveryAddressesActivity;
import com.abuubaida921.esupermarkt.model.DeliveryAddressModel;
import com.abuubaida921.esupermarkt.model.OrderItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryAddressItemAdapter extends RecyclerView.Adapter<DeliveryAddressItemAdapter.viewHolder> {

    Context context;
    ArrayList<DeliveryAddressModel> arrayList;

    public DeliveryAddressItemAdapter(Context context, ArrayList<DeliveryAddressModel> OrderItemModel) {
        this.context = context;
        this.arrayList = OrderItemModel;
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

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomThemeDialogueTheme);
                View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.address_bottom_sheet_layout, v.findViewById(R.id.checkout_bottom_sheet_layout));
                EditText edit_address = bottomSheetView.findViewById(R.id.edit_address);
                Button add_d_address_btn = bottomSheetView.findViewById(R.id.add_d_address_btn);
                edit_address.setText(arrayList.get(pos).getDelivery_address());
                add_d_address_btn.setText("Update");

                add_d_address_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        long key = Long.parseLong(arrayList.get(pos).getId());

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("delivery_address", edit_address.getText().toString());

                        FirebaseDatabase.getInstance().getReference("delivery_address").child(key+"").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Address Updated", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        });

                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
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

