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

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.ProductViewActivity;
import com.abuubaida921.esupermarkt.model.CartItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.abuubaida921.esupermarkt.model.FaqModel;
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

public class FaqItemAdapter extends RecyclerView.Adapter<FaqItemAdapter.viewHolder> {

    Context context;
    ArrayList<FaqModel> arrayList;

    public FaqItemAdapter(Context context, ArrayList<FaqModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.expandable_listview_item_layout, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {
        boolean isExpandable = arrayList.get(viewHolder.getAdapterPosition()).getExpandable();
        viewHolder.description.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        viewHolder.title.setText(arrayList.get(viewHolder.getAdapterPosition()).getTitle());
        viewHolder.description.setText(arrayList.get(viewHolder.getAdapterPosition()).getDescription());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaqModel faqModel = arrayList.get(viewHolder.getAdapterPosition());
                faqModel.setExpandable(!faqModel.getExpandable());
                notifyItemChanged(viewHolder.getAdapterPosition());
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
        TextView title, description;
        CardView cardView;

        public viewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}

