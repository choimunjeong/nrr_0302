package com.example.page3_0226;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        Context context;
        List<Recycler_item> items;
        int item_layout;
public RecyclerAdapter(Context context, List<Recycler_item> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.page3_1_x_cardview,null);
        return new ViewHolder(v);
        }

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@Override
public void onBindViewHolder(ViewHolder holder, int position) {
final Recycler_item item=items.get(position);
        Drawable drawable=context.getResources().getDrawable(item.getImage());
        holder.image.setBackground(drawable);
        holder.title.setText(item.getTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Toast.makeText(context,item.getTitle(),Toast.LENGTH_SHORT).show();
        }
        });
        }

@Override
public int getItemCount() {
        return this.items.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView title;
    CardView cardview;

    public ViewHolder(View itemView) {
        super(itemView);
        image=(ImageView)itemView.findViewById(R.id.image);
        title=(TextView)itemView.findViewById(R.id.title);
        cardview=(CardView)itemView.findViewById(R.id.page3_1_x_cardview);
    }
}

    private class Recycler_item {
            int image;
            String title;

            int getImage(){
                return this.image;
            }
            String getTitle(){
                return this.title;
            }

            Recycler_item(int image, String title){
                this.image=image;
                this.title=title;
            }

    }
    }

