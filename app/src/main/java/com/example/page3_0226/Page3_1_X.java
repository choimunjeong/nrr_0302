package com.example.page3_0226;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Page3_1_X extends AppCompatActivity {
    TextView page3_1_x_stationName;
    RecyclerView page3_1_x_recyclerview;
    ProgressBar progressBar;

    String url;
    String st_name;                           //앞에서 전달 받은 역이름
    String readStr = "";                      //txt에서 가져온 문자를 담는다.
    String[] arr_line = null;
    String[] _name = new String[3];           //txt에서 받은 역이름
    String[] _areaCode = new String[3];       //txt에서 받은 지역코드
    String[] _sigunguCode = new String[3];    //txt에서 받은 시군구코드

    //url 배열부분(쪼개고 쪼개는 과정이라서 왕 많아짐
    String name_1[] = new String[5];
    String name_2[] = new String[5];
    String name[] = new String[5];
    String img_Url[] = new String[5];
    String contentid[] = new String[5];

    String areaCode, sigunguCode;            //전달받은 역의 지역코드, 시군구코드
    List<Page3_1_x_Adapter.Item2> items = new ArrayList<>();

    //왜인지 모르겠는데 그냥 api에서 받은 url로는 이미지가 안뜸, 아래 url붙이면 뜸
    String Url_front = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_1_x);

        page3_1_x_stationName = (TextView)findViewById(R.id.page3_1_x_stationName);
        page3_1_x_recyclerview = (RecyclerView) findViewById(R.id.page3_1_x_recyclerview);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //리사이클러뷰 연결
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        page3_1_x_recyclerview.setHasFixedSize(true);
        page3_1_x_recyclerview.setLayoutManager(linearLayoutManager);

        //앞 액티비티에서 전달 값 받기
        Intent intent = getIntent();
        st_name = intent.getStringExtra("st_name");
        page3_1_x_stationName.setText(st_name);
        //txt 값 읽기
        settingList();

        //전달된 역의 지역코드, 시군구코드 찾기
        compareStation();

        //관광 api 실행
        SearchTask task = new SearchTask();
        task.execute();

    }

    //txt 돌려 역 비교할 배열 만들기(이름 지역코드 동네코드)<-로 구성
    private void settingList(){
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;
        try{
            inputStream = assetManager.open("station_code.txt");
            //버퍼리더에 대한 설명 참고 : https://coding-factory.tistory.com/251
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            while (((str = reader.readLine()) != null)){ readStr += str + "\n";}
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] arr_all = readStr.split("\n"); //txt 내용을 줄바꿈 기준으로 나눈다.

        //한 줄의 값을 띄어쓰기 기준으로 나눠서, 역명/지역코드/시군구코드 배열에 넣는다.
        for(int i=0; i <arr_all.length; i++) {
            arr_line = arr_all[i].split(" ");

            _name[i] = arr_line[0];         //서울
            _areaCode[i] = arr_line[1];     //1
            _sigunguCode[i] = arr_line[2];  //0
        }
    }

    //앞 액티비티에서 선택된 역과 같은 역을 찾는다.
    private void compareStation(){
        for(int i=0; i<_name.length; i++){
            if(st_name.equals(_name[i])){
                areaCode = _areaCode[i];        //string으로 되어있는 걸 int로 형변환
                sigunguCode = _sigunguCode[i];
            }
        }
    }

    //관광api 연결
    class SearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //초기화 단계에서 사용
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("시작", "시작");

            //시군구코드가 0 일 때와 0이 아닐때를 구분해서 url을 넣어준다.
            if(sigunguCode.equals("0")){
                url = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey=" +
                        "7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D" +
                        "&pageNo=1&numOfRows=5&MobileApp=AppTest&MobileOS=ETC&arrange=A&contentTypeId=12&" +
                        "sigunguCode=" +
                        "&areaCode=" + areaCode +
                        "&listYN=Y";
            } else {
                url = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey=" +
                        "7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D" +
                        "&pageNo=1&numOfRows=5&MobileApp=AppTest&MobileOS=ETC&arrange=A&contentTypeId=12&" +
                        "sigunguCode=" + sigunguCode +
                        "&areaCode=" + areaCode +
                        "&listYN=Y";
            }

            URL xmlUrl;
            String returnResult = "";

            try {
                boolean title = false;
                boolean firstimage = false;
                boolean item = false;
                boolean contentid = false;

                xmlUrl = new URL(url);
                Log.d("url", url);
                xmlUrl.openConnection().getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG: {
                            if (parser.getName().equals("item")) {
                                item = true;
                            }
                            if (parser.getName().equals("contentid")){
                                contentid = true;
                                Log.d("태그 시작", "태그 시작2");
                            }
                            if (parser.getName().equals("firstimage")){
                                firstimage = true;
                                Log.d("태그 시작", "태그 시작3");
                            }
                            if (parser.getName().equals("title")) {
                                title = true;
                                Log.d("태그 시작", "태그 시작4");
                            }
                            break;
                        }

                        case XmlPullParser.TEXT: {
                            if(contentid){
                                returnResult += parser.getText() + "  ";
                                contentid = false;
                            }
                            if(firstimage){
                                returnResult += parser.getText() + "  ";
                                firstimage = false;
                            }
                            if (title) {
                                returnResult += parser.getText() + "\n";
                                Log.d("태그 받음", "태그받음4");
                                title = false;
                            }
                            break;
                        }
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {
                                break;
                            }

                        case XmlPullParser.END_DOCUMENT:
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("err", "erro");
            }
            return returnResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.INVISIBLE);

            //카드뷰 구현 부분
            //사진링크, 타이틀(관광명), 분야뭔지 분리
            Log.i("전달 받은 값", result);
            //사진링크, 타이틀(관광명), 분야뭔지 분리
            name_1 = result.split("\n");
            Log.d("네임11", name_1[1] + "  " + name_1.length);

            for(int i=0; i<name_1.length; i++){
                name_2 = name_1[i].split("  ");
                if(name_2.length != 2){
                    contentid[i] = name_2[0];
                    img_Url[i] = name_2[1];
                    name[i] = name_2[2];
                    Log.i("아 뭔데 ;;", i+"번째"+name_2[0]+name_2[1]);
                } else {
                    contentid[i] = name_2[0];
                    img_Url[i] = null;
                    name[i] = name_2[1];
                    Log.i("아 뭔데 ;;", i+"번째"+name_2[0]);
                }
            }
            Log.d("imageㅕ기", name[0]+name[1]+name[2]+name[3]);
            Page3_1_x_Adapter.Item2[] item=new Page3_1_x_Adapter.Item2[5];
            for(int i=0;i<5;i++) {
                item[i]=new Page3_1_x_Adapter.Item2(Url_front+img_Url[i],name[i],contentid[i]);
            }
            for(int i=0;i<5;i++) {
                items.add(item[i]);
            }
            page3_1_x_recyclerview.setAdapter(new Page3_1_x_Adapter(getApplicationContext(),items));

        }
    }
}
