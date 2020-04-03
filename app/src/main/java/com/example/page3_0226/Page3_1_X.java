package com.example.page3_0226;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Page3_1_X extends AppCompatActivity implements Page3_1_x_OnItemClick{

    //역 이름을 받아서 지역코드랑 시군구코드 받기 위한 배열(현재 3개 지역만 넣어놔서 배열크기가 3임)
    String[] arr_line = null;
    String[] _name = new String[3];           //txt에서 받은 역이름
    String[] _areaCode = new String[3];       //txt에서 받은 지역코드
    String[] _sigunguCode = new String[3];    //txt에서 받은 시군구코드
    String[] _x = new String[3];              //txt에서 받은 x좌표
    String[] _y = new String[3];              //txt에서 받은 y좌표
    String[] _benefitURL = new String[3];     //txt에서 받은 혜택url
    String st_name, areaCode, sigunguCode, benefitURL;            //전달받은 역의 지역코드, 시군구코드, 혜택URL
    Double x, y;                                      //전달받은 역의 x,y 좌표

    String name_1[] = new String[20];  //returnResult를 줄바꿈 단위로 쪼개서 넣은 배열/ name_1[0]에는 한 관광지의 이름,url,contentId,위치가 다 들어가 있다.
    String name_2[] = new String[5];  //name_1를 "  " 단위로 쪼개서 넣은 배열/ [0]= contentID/ [1]=mapx/ [2]에= mapy/ [3]= img_Url/ [4]= name이 들어가 있다.

    //xml 파싱한 값을 분류해서 쪼개 넣음
    String name[] = new String[20];        //관광지 이름
    String img_Url[] = new String[20];     //이미지 URL
    String contentid[] = new String[20];   //관광지ID
    String mapx[] = new String[20];        //X좌표
    String mapy[] = new String[120];        //Y좌표
    int length = name_1.length;

    String returnResult, url;
    String Url_front = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=";

    List<Recycler_item> items = new ArrayList<>();

    RecyclerView recyclerView;
    AppBarLayout appBarLayout;
    MapView mapView;
    MapPOIItem marker;
    ViewGroup mapViewContainer;
    Button reset_btn;
    TextView page3_1_x_region, benefit;
    ImageView benefit_url;

    private DbOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_1_x);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        page3_1_x_region = (TextView)findViewById(R.id.page3_1_x_region);
        benefit = (TextView)findViewById(R.id.benefit);
        benefit_url = (ImageView)findViewById(R.id.benefit_url);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    Log.d("접혔어!!!!", "접혔어!!");
                    page3_1_x_region.setVisibility(View.VISIBLE);
                    benefit.setVisibility(View.VISIBLE);
                    benefit_url.setVisibility(View.VISIBLE);
                } else if (verticalOffset == 0) {
                    // Expanded
                    Log.d("확장됐어!!", "확장쓰!!");

                } else {
                    // Somewhere in between
                    Log.d("중간이야!!!", "중간이야!!!!");
                    page3_1_x_region.setVisibility(View.INVISIBLE);
                    benefit.setVisibility(View.INVISIBLE);
                    benefit_url.setVisibility(View.INVISIBLE);
                }
            }
        });


        //앞 액티비티에서 전달 값 받기
        Intent intent = getIntent();
        st_name = intent.getStringExtra("st_name");
        page3_1_x_region.setText(st_name);

        //txt 값 읽기
        settingList();

        //전달된 역의 지역코드, 시군구코드 찾기
        compareStation();


        //혜택 URL로 넘기는 부분
        benefit_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //혜택이 없을 경우
                if(benefitURL.equals("혜택없음")){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Page3_1_X.this);
                    alertDialogBuilder .setMessage(st_name + "역은 혜택이 없습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick( DialogInterface dialog, int id) {
                                    // 프로그램을 종료한다
                                   dialog.cancel(); } });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialogBuilder.show();
                }

                //있을 경우, url로 연결해준다.
                else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(benefitURL));
                    startActivity(intent);
                }


            }
        });


        //세로 드래그 문제를 해결하기 위한 부분
        //https://do-dam.tistory.com/entry/CoordinatorLayout-App-Bar-%EB%93%9C%EB%9E%98%EA%B7%B8-%EB%B9%84%ED%99%9C%EC%84%B1%ED%99%94-%EC%83%81%EB%8B%A8-%EC%8A%A4%ED%81%AC%EB%A1%A4-%EA%B5%AC%ED%98%84
        AppBarLayout appBar = (AppBarLayout) findViewById(R.id.app_bar);
        if (appBar.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }


        //툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //맵뷰
        //카카오맵은 1개만 선언할 수 있대.
        mapView = new MapView(this);
        mapViewContainer.addView(mapView,0);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(y, x), 8, true);
        marker = new MapPOIItem();


        //리사이클러뷰 구현 부분
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        //맵 리셋버튼
        reset_btn = (Button)findViewById(R.id.reset_btn);
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.566297, 126.977946), 8, true);
            }
        });


        //관광 api 연결 부분
        SearchTask task = new SearchTask();
        try {
            String RESULT = task.execute().get();
            Log.i("전달 받은 값", RESULT);


            //사진링크, 타이틀(관광명), 분야뭔지 분리
            name_1 = RESULT.split("\n");
            Log.i("여기다 문정아ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ",Integer.toString(length));
            for (int i = 0; i < length; i++) {
                name_2 = name_1[i].split("  ");

                //img_Url이 없는 경우도 있기 때문에, 길이=5=있음/ 길이=4=없음
                if (name_2.length == 5) {
                    contentid[i] = name_2[0];
                    img_Url[i] = name_2[1];
                    mapx[i] = name_2[2];
                    mapy[i] = name_2[3];
                    name[i] = name_2[4];
                } else {
                    contentid[i] = name_2[0];
                    img_Url[i] = null;
                    mapx[i] = name_2[1];
                    mapy[i] = name_2[2];
                    name[i] = name_2[3];
                }
            }

            Recycler_item[] item = new Recycler_item[length];
            for (int i = 0; i < length; i++) {
                item[i] = new Recycler_item(Url_front + img_Url[i], name[i], contentid[i], mapx[i], mapy[i]);

                //마커 많이 만들기
                double X = Double.parseDouble(mapx[i]);
                double Y = Double.parseDouble(mapy[i]);
                marker.setTag(1);
                marker.setItemName(name[i]);
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Y, X));
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                mapView.addPOIItem(marker);
            }
            for (int i = 0; i < length; i++) {
                items.add(item[i]);
            }
            recyclerView.setAdapter(new Page3_1_x_Adapter(getApplicationContext(), items, this));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
    }



    //txt 돌려 역 비교할 배열 만들기(이름 지역코드 동네코드)<-로 구성
    private void settingList(){
        String readStr = "";
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
            _y[i] = arr_line[3];            //y좌표
            _x[i] = arr_line[4];            //x좌표
            _benefitURL[i] = arr_line[5];
        }
    }


    //앞 액티비티에서 선택된 역과 같은 역을 찾는다.
    private void compareStation(){
        for(int i=0; i<_name.length; i++){
            if(st_name.equals(_name[i])){
                areaCode = _areaCode[i];
                sigunguCode = _sigunguCode[i];
                y = Double.parseDouble(_y[i]);     //string으로 되어있는 걸 double로 형변환
                x = Double.parseDouble(_x[i]);
                benefitURL = _benefitURL[i];
            }
        }
    }


    //이 클래스는 어댑터와 서로 주고받으며 쓰는 클래스임
    public class Recycler_item {
        String image;
        String title;
        String contentviewID;
        String mapx;
        String mapy;

        String getImage() {
            return this.image;
        }

        String getTitle() {
            return this.title;
        }

        String getContentviewID() {
            return this.contentviewID;
        }

        String getMapx() {
            return this.mapx;
        }

        String getMapy() {
            return this.mapy;
        }

        Recycler_item(String image, String title, String contentviewID, String mapx, String mapy) {
            this.image = image;
            this.title = title;
            this.contentviewID = contentviewID;
            this.mapx = mapx;
            this.mapy = mapy;
        }
    }


    //관광api 연결
    class SearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //초기화 단계에서 사용
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("시작", "시작");

            //시군구코드가 0 일 때와 0이 아닐때를 구분해서 url을 넣어준다.
            if(sigunguCode.equals("0")){
                url = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey=" +
                        "7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D" +
                        "&pageNo=1&numOfRows=20&MobileApp=AppTest&MobileOS=ETC&arrange=A&contentTypeId=12&" +
                        "sigunguCode=" +
                        "&areaCode=" + areaCode +
                        "&listYN=Y";
            } else {
                url = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey=" +
                        "7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D" +
                        "&pageNo=1&numOfRows=20&MobileApp=AppTest&MobileOS=ETC&arrange=A&contentTypeId=12&" +
                        "sigunguCode=" + sigunguCode +
                        "&areaCode=" + areaCode +
                        "&listYN=Y";
            }
//
//            String url = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey=7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D" +
//                    "&pageNo=1&numOfRows=10&MobileApp=AppTest&MobileOS=ETC&arrange=A&contentTypeId=12&sigunguCode=&areaCode=1&listYN=Y";

            URL xmlUrl;
            returnResult = "";
            String re = "";

            try {
                boolean title = false;
                boolean firstimage = false;
                boolean item = false;
                boolean contentid = false;
                boolean mapx = false;
                boolean mapy = false;

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
                            if (parser.getName().equals("contentid")) {
                                contentid = true;
                                Log.d("태그 시작", "태그 시작2");
                            }
                            if (parser.getName().equals("mapx")) {
                                mapx = true;
                            }
                            if (parser.getName().equals("mapy")) {
                                mapy = true;
                            }
                            if (parser.getName().equals("firstimage")) {
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
                            if (contentid) {
                                returnResult += parser.getText() + "  ";
                                contentid = false;
                            }
                            if (mapx) {
                                returnResult += parser.getText() + "  ";
                                mapx = false;
                            }
                            if (mapy) {
                                returnResult += parser.getText() + "  ";
                                mapy = false;
                            }
                            if (firstimage) {
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
        }
    }


    //https://www.charlezz.com/?p=768
    //어댑터에서 액티비티 함수를 쓸 수 있는 인터페이스 만듦
    @Override
    public void onClick(double x, double y, String name) {
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(y, x), 3, true);
        marker.setTag(1);
        marker.setItemName(name);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(y, x));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
        Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
    }



    //인터페이스 부분/ db에 넣는다.
    @Override
    public void make_db(String countId, String name) {
        mDbOpenHelper.open();
        mDbOpenHelper.insertColumn(countId, name);
        mDbOpenHelper.close();
    }



    //인터페이스를 사용하는 이유 : 어댑터에서 함수를 사용하기가 까다로워서 -> 메인액티비티에서 함수를 만들고 어댑터에서 접근할 수 있도록 함
    @Override
    public void make_dialog() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("관심관광지 추가 성공");
        builder.setMessage("관심관광지 목록을 확인하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //관심관광지 페이지로 감
                Intent intent = new Intent(Page3_1_X.this, Heart_page.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }


}
