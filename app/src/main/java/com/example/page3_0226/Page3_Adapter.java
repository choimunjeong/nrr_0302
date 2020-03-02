package com.example.page3_0226;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Page3_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //헤더인지 아이템인지 확인하는데 필요함
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    //어댑터에 들어갈 것들 선언
    private final List<Item> data;
    private final Context context;

    //이 부분을 MainActivity에서 활용할 것
    public Page3_Adapter(List<Item> data, Context context){
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //xml를 inflate시키는 단계 = 선언, 틀만 만들어놓는 단계
        View view = null;
        View view2 = null;
        switch ( viewType) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.page3_1_list_header, parent, false);
                ListHeaderViewHolder listHeaderViewHolder = new ListHeaderViewHolder(view);
                return listHeaderViewHolder;
            case CHILD:
                LayoutInflater inflater2 = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view2 = inflater2.inflate(R.layout.page3_1_list_item, parent,false);
                ListChildViewHolder listChildViewHolder = new ListChildViewHolder(view2);
                return  listChildViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //함수 돌아가면서 xml에 적용(bind)되는 부분임
        final  Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder listHeaderViewHolder = (ListHeaderViewHolder) holder;
                listHeaderViewHolder.refferalItem = item;
                listHeaderViewHolder.header_title.setText(item.text);

//                //헤더 옆 ( +/-)이미지를 바꾸는 부분
                if(item.invisibleChildren == null) {
                    listHeaderViewHolder.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                } else {
                    listHeaderViewHolder.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                }

                //헤더 부분이 터치 -> 접었다가 펼치는 부분
                listHeaderViewHolder.header_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //invisibleChildren에 접히는 부분 data를 저장하는 것 같네
                        //invisibleChildren이 null이라는 것은 그 안에 data가 없다 = 펼쳐져 있다.
                        if(item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(listHeaderViewHolder.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD){
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            //특정 영역에 데이터를 제거하는 것.
                            notifyItemRangeRemoved(pos + 1, count);
                            listHeaderViewHolder.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                        } else {
                            int pos = data.indexOf(listHeaderViewHolder.refferalItem);
                            int index = pos + 1;
                            for ( Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            listHeaderViewHolder.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                //역 부분에 데이터 넣어줌
                TextView itemTextView = (TextView)((ListChildViewHolder)holder).list_sttationText;
                itemTextView.setText(data.get(position).text);

                //역이 선택되면 다음 페이지 넘어가가면서 값도 넘겨줄거임.
                itemTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //프로그레스 다이얼로그
                        final ProgressDialog asyncDialog = new ProgressDialog( v.getContext());
                        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        asyncDialog.setMessage(data.get(position).text+"(으)로 이동중입니다..");
                        asyncDialog.show();

                        //터치된 역명을 다음 액티비티로 전달
                        Intent intent = new Intent(context, Page3_1_X.class);
                        intent.putExtra("st_name", data.get(position).text);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //이게 없으면 오류남
                        context.startActivity(intent);

                        //0.5초 후, 다이얼로그 없앰
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable()  {
                            public void run() {
                                // 시간 지난 후 실행할 코딩
                                asyncDialog.cancel();
                            }
                        }, 500); // 0.5초후
                    }
                });
        }
    }

    @Override
    final public int getItemViewType(int position) { return data.get(position).type; }

    @Override
    public int getItemCount() { return data.size();}

    //헤더 xml 부분(page3_1_list_header)을 어댑터와 연결해주는 뷰홀더임
    private  static class ListHeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView header_title;
        public ImageView btn_expand_toggle;
        public Item refferalItem;  //밑에서 직접 클라스 선언해줌
        public LinearLayout list_header;  //헤어부분 레이아웃을 클릭하면 반응하기 위해 추가

        public ListHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            header_title = (TextView)itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView)itemView.findViewById(R.id.btn_expand_toggle);
            list_header = (LinearLayout)itemView.findViewById(R.id.list_header);
        }
    }

    //촤일드 xml 부분(page3_1_list_item)을 어댑터와 연결해주는 뷰홀더.
    private static class ListChildViewHolder extends RecyclerView.ViewHolder{
        public TextView list_sttationText;   //역 이름

        public ListChildViewHolder(@NonNull View itemView) {
            super(itemView);
            list_sttationText = (TextView)itemView.findViewById(R.id.list_stationText);
        }
    }

    public static class Item {
        public  int type;
        public  String text;
        public List<Item> invisibleChildren;

        public Item(int type) { this.type = type; }

        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }
    }
}
