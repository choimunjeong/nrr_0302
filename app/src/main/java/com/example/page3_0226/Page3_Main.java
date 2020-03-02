package com.example.page3_0226;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Page3_Main extends AppCompatActivity {

    ViewPager viewPager;
    Page3_VPAdapter adapter;
    TabLayout tabLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_main);


        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.tablayout);


        adapter=new Page3_VPAdapter(getSupportFragmentManager()); //Viewpager와 Fragment를 연결
        tabLayout.setupWithViewPager(viewPager); //Viewpager와 TabLayout을 연결해주는 코드
        viewPager.setAdapter(adapter); //Viewpager에 선택된 fragment를 띄워준다.
    }
}
