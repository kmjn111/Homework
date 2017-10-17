package com.example.user.homework;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MyAdapter adapter;
    ArrayList<MyItem> data = new ArrayList<MyItem>();

    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview = (TextView) findViewById(R.id.call_number);

        //전화하기 버튼 연결 및 클릭 시 전화하기 기능 연결
        ImageButton Imgbtn = (ImageButton) findViewById(R.id.imgbtn);
        Imgbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent implicit_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+textview.getText()));
                startActivity(implicit_intent);
            }

        });

        //메뉴리스트 추가
        data.add(new MyItem(R.drawable.spotato, "불갈비만두피자","8,000원","평점 4.2 점"));
        data.add(new MyItem(R.drawable.cheese, "치즈피자","5,000원","평점 3.4 점"));
        data.add(new MyItem(R.drawable.pepper, "페퍼로니피자","5,000원","평점 3.0 점"));
        data.add(new MyItem(R.drawable.spotato, "고구마피자","6,000원","평점 4.5 점"));
        data.add(new MyItem(R.drawable.spotato, "직화파인애플피자","8,000원","평점 3.9 점"));

        //어댑터를 통하여 리스트뷰에 데이터 넣기
        adapter = new MyAdapter(this, R.layout.item ,data);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        //메뉴를 눌렀을 때 cheeseActivity 로 보내줄 데이터 셋팅 및 cheeseActivity 열기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getApplicationContext(), cheeseActivity.class);

                intent.putExtra("title", data.get(position).nName);
                intent.putExtra("img", data.get(position).mIcon);
                intent.putExtra("price", data.get(position).nAge);
                intent.putExtra("value", data.get(position).nValue);

                startActivity(intent);
            }
        });
    }
}

//커스텀리스트 뷰에 담을 아이템 정보 클래스 선언
class MyItem {
    int mIcon; // image resource
    String nName; // text
    String nAge;  // text
    String nValue;

    MyItem(int aIcon, String aName, String aAge, String aValue) {
        mIcon = aIcon;
        nName = aName;
        nAge = aAge;
        nValue = aValue;
    }
}