package com.abuubaida921.esupermarkt.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abuubaida921.esupermarkt.R;
import com.abuubaida921.esupermarkt.activity.CategoryWiseProductViewActivity;
import com.abuubaida921.esupermarkt.activity.ProductViewActivity;
import com.abuubaida921.esupermarkt.model.CategoryItemModel;
import com.abuubaida921.esupermarkt.model.ExclusiveOfferItemModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.viewHolder> {


    Context context;
    ArrayList<CategoryItemModel> arrayList;

    public CategoryItemAdapter(Context context, ArrayList<CategoryItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_view_card, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, int position) {

        viewHolder.item_name.setText(arrayList.get(viewHolder.getAdapterPosition()).getName());
        viewHolder.relative_layout.setBackgroundColor(Color.parseColor(arrayList.get(viewHolder.getAdapterPosition()).getItem_holder_color_code()));

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
                Intent intent = new Intent(context, CategoryWiseProductViewActivity.class);
                intent.putExtra("id", arrayList.get(viewHolder.getAdapterPosition()).getId());
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
        TextView item_name;
        CardView cardView;
        ImageView item_image;
        RelativeLayout relative_layout;

        public viewHolder(View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_img);
            item_name = itemView.findViewById(R.id.txt_name);
            relative_layout = itemView.findViewById(R.id.relative_layout);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}

