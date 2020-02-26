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

import java.util.List;

public class Page3_1_x_Adapter extends RecyclerView.Adapter<Page3_1_x_Adapter.ViewHolder> {
    String state = "OFF";   //하트의 클릭 여부
    Context context;
    List<Item2> data;

    //메인에서 접근할 때, 이 부분을 씀
    public Page3_1_x_Adapter(Context context, List<Item2> data) {
        this.context = context;
        this.data = data;   //리스트
    }

    @Override
    public Page3_1_x_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.page3_1_x_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Item2 item = data.get(position);
        //이미지 뷰에 url 이미지 넣기
        //gradle(project)->repositories 에 mavenCentral() 넣어줌
        //gradle(app)->dependecied 에 아래 두줄 넣어줌
        //    implementation 'com.github.bumptech.glide:glide:4.9.0'
        //    annotationProcessor 'com.github.bumptech.glide:glide:4.9.0'
        Glide.with(context).load(item.getImage()).centerCrop().into(holder.image);
        holder.title.setText(item.getTitle());

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (state == "OFF") {
                    holder.heart.setBackgroundResource(R.drawable.cardview_heart);
                    state = "ON";
                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    holder.heart.setBackgroundResource(R.drawable.cardview_heart_off);
                    state = "OFF";
                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //여기에 리스트를 클릭하면, 관광지 상세페이지로 넘어가는거 구현
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getContentviewID(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Page3_1_X_X.class);

                //이 부분에서 contentId 넘기고, 다음 액티비티에서 url로 바로 연결시키면 될 듯
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;   //관광지 사진
        TextView title;    //관광지 이름
        CardView cardview;
        Button heart;       //하트 버튼

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.page3_1_x_image);
            title = (TextView) itemView.findViewById(R.id.page3_1_x_title);
            cardview = (CardView) itemView.findViewById(R.id.page3_1_x_cardview);
            heart = (Button) itemView.findViewById(R.id.page3_1_x_heart_btn);
        }
    }

    public static class Item2 {
        String image;
        String title;
        String contentviewID;

        String getImage() {
            return this.image;
        }

        String getTitle() {
            return this.title;
        }

        String getContentviewID() {
            return this.contentviewID;
        }


        Item2(String image, String title, String contentviewID) {
            this.image = image;
            this.title = title;
            this.contentviewID = contentviewID;
        }
    }
}
