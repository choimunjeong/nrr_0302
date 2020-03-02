package com.example.page3_0226;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Page3_Fragment2 extends Fragment {

    private RecyclerView page3_recyclerview;
    private Page3_Adapter page3_adapter;


    public Page3_Fragment2() {
        // Required empty public constructor
    }


    public static Page3_Fragment2 newInstance(){
        Bundle args = new Bundle();
        Page3_Fragment2 fragment = new Page3_Fragment2();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.page3_fragment2, container, false);


        page3_recyclerview = (RecyclerView)v.findViewById(R.id.page3_recyclerview);
        page3_recyclerview.setHasFixedSize(true);   //리사이클러뷰 사이즈를 고정


        final List<Page3_Adapter.Item> data = new ArrayList<>();
        final Context context = getActivity();


        //데이터 몇개 없으니깐 직접 추가. 나중에 역 많으면 txt롤 받고 for문으로 추가해줄 수 있음
        data.add(new Page3_Adapter.Item(Page3_Adapter.HEADER, "수도권"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "서울"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "신탄리"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "대광리"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "연천"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "한탄강"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "초성리"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "소요산"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "동두천"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "강촌"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "문산"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "파주"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "도라산"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "금촌"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "의정부"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "대성리"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "청평"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "일영"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "원릉"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "삼릉"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "사릉"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "대곡"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "평내호평"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "퇴계원"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "곡산"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "금곡"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "행신"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "강매"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "화전"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "도농"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "일산"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "덕소"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "가좌"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "양수"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "용산"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "아신"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "국수"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "석불"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "노량진"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "양평"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "용문"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "원덕"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "일신"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "양동"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "의왕"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "오산"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "송탄"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "평택"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "수색"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "서정리"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "신원"));
        data.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,  "인천"));

        Page3_Adapter.Item places1 = new Page3_Adapter.Item(Page3_Adapter.HEADER, "충청권");
        places1.invisibleChildren = new ArrayList<>();
        places1.invisibleChildren.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "제천"));
        places1.invisibleChildren.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "대곡"));
        places1.invisibleChildren.add(new Page3_Adapter.Item(Page3_Adapter.CHILD, "청주"));
        places1.invisibleChildren.add(new Page3_Adapter.Item(Page3_Adapter.CHILD,"오송"));
        data.add(places1);

        Page3_Adapter.Item places2 = new Page3_Adapter.Item(Page3_Adapter.HEADER, "충청권");
        places2.invisibleChildren = new ArrayList<>();
        data.add(places2);

        Page3_Adapter.Item places3 = new Page3_Adapter.Item(Page3_Adapter.HEADER, "충청권");
        places3.invisibleChildren = new ArrayList<>();
        data.add(places3);

        Page3_Adapter.Item places4 = new Page3_Adapter.Item(Page3_Adapter.HEADER, "충청권");
        places4.invisibleChildren = new ArrayList<>();
        data.add(places4);


        //아이템부분 격자롤 보여주기 위한 부분
        //헤드포함해서 칸이 4개인데 헤드
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //만약 헤더면 칸 4개를 다 먹는다.
                if(Page3_Adapter.HEADER == page3_adapter.getItemViewType(position)){ return 4; }
                //아이템들은 한 칸씩 차지
                else return 1;
            }
        });


        page3_recyclerview.setLayoutManager(gridLayoutManager);  //그리드레이아웃 적용
        page3_adapter = new Page3_Adapter(data, context);  //data와 context로 어댑터와 연결
        page3_recyclerview.setAdapter(page3_adapter);


        //구분선 넣기
        page3_recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        return v;
    }

}
