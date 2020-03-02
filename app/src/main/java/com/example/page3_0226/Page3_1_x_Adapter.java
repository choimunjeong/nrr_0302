package com.example.page3_0226;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

public class Page3_1_x_Adapter extends RecyclerView.Adapter<Page3_1_x_Adapter.ViewHolder> {
    private String stay = "OFF";  // 하트의 클릭 여부
    private Context context;
    private List<Page3_1_X.Recycler_item> items;  //리사이클러뷰 안에 들어갈 값 저장
    private Page3_1_x_OnItemClick mCallback;

    //메인에서 불러올 때, 이 함수를 씀
    public Page3_1_x_Adapter(Context context, List<Page3_1_X.Recycler_item> items, Page3_1_x_OnItemClick mCallback) {
        this.context=context;
        this.items=items;   //리스트
        this.mCallback=mCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.page3_1_x_item_cardview,null);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Page3_1_X.Recycler_item item=items.get(position);

        //이미지뷰에 url 이미지 넣기.
        Glide.with(context).load(item.getImage()).centerCrop().into(holder.image);
        holder.title.setText(item.getTitle());

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(stay=="OFF"){
                    holder.heart.setBackgroundResource(R.drawable.cardview_heart);
                    stay = "ON";
                    Toast.makeText(context,item.getTitle(),Toast.LENGTH_SHORT).show();
                } else{
                    holder.heart.setBackgroundResource(R.drawable.cardview_heart_off);
                    stay = "OFF";
                    Toast.makeText(context,item.getTitle(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        //여기에 리스트를 클릭하면, 관광지 상세페이지로 넘어가는거 구현
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,item.getContentviewID(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Page3_1_X_X.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        //맵 띄우는 버튼 -> x, y좌표 전달 + 맵을 위에서 끌어내림
        holder.pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,item.getContentviewID()+"------"+item.getMapx()+"------"+item.getMapy(),Toast.LENGTH_SHORT).show();

                //맵을 위에서 끌어내리는 부분
                AppBarLayout appBarLayout=(AppBarLayout)((Page3_1_X)v.getContext()).findViewById(R.id.app_bar);
                appBarLayout.setExpanded(true);

                //터치된 해당 관광지 좌표 전달
                double x = Double.parseDouble(item.getMapx());
                double y = Double.parseDouble(item.getMapy());

                if(mCallback!=null){
                    mCallback.onClick(x,y, item.getTitle());
                }
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
        Button heart;
        Button pin;


        public ViewHolder(View itemView) {
            super(itemView);
            heart = (Button)itemView.findViewById(R.id.cardview_heart);
            image=(ImageView)itemView.findViewById(R.id.image);
            title=(TextView)itemView.findViewById(R.id.title);
            cardview=(CardView)itemView.findViewById(R.id.cardview);
            pin=(Button)itemView.findViewById(R.id.cardview_pin);

        }
    }

}