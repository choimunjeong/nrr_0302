package com.example.page3_0226;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Page3_VPAdapter  extends FragmentPagerAdapter {

    Page3_VPAdapter(FragmentManager fm){
        super(fm);
    } //꼭 있어야함

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Page3_Fragment1.newInstance();
            case 1:
                return Page3_Fragment2.newInstance();
        }
        return null;
    }
    private static int PAGE_NUMBER=2; //생성할 프래그먼트 수
    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    //탭 레이아웃의 제목을 지정해준다
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "지도로 보기";
            case 1:
                return"역명으로 보기";
            default:
                return null;
        }
    }
}

