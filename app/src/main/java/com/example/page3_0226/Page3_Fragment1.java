package com.example.page3_0226;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class Page3_Fragment1 extends Fragment {

    Handler handler =new Handler();
    TextView textView;
    Context context = this.getContext();


    public Page3_Fragment1() {
        // Required empty public constructor
    }


    private WebSettings webSettings;
    public static Page3_Fragment1 newInstance(){
        Bundle args = new Bundle();
        Page3_Fragment1 fragment = new Page3_Fragment1();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.page3_fagment1, container, false);

        textView = (TextView)v.findViewById(R.id.text);
        WebView web = (WebView)v.findViewById(R.id.web_line_map);


        //웹뷰 자바스크립트 사용가능하도록 선언
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setDisplayZoomControls(false);  //웹뷰 돋보기 없앰


        //웹뷰 줌기능
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setSupportZoom(true);


        //자바스크립트에서 메시지를 받을 수 있게 함 + 글자 비교해서 이미지 나오도록
        web.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void send(final String msg){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        textView.setText(msg);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), Page3_1_X.class);
                        intent.putExtra("st_name", msg);
                        startActivity(intent);
                    }});
            }
        }, "android");


        //웹뷰 화면 비율 맞추기
        web.setInitialScale(230);


        //웹뷰를 로드함
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("file:///android_asset/web.html");


        return v;
    }


}
