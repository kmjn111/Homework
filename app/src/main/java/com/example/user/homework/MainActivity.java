package com.example.user.homework;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.user.homework.R.id.parent;

public class MainActivity extends AppCompatActivity {
    static MyAdapter adapter;
    ArrayList<MyItem> data = new ArrayList<MyItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton Imgbtn = (ImageButton) findViewById(R.id.imgbtn);
        Imgbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent implicit_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:027633332"));
                startActivity(implicit_intent);
            }

        });
        data.add(new MyItem(R.drawable.spotato, "불갈비만두피자","8,000원","평점 4.2 점"));
        data.add(new MyItem(R.drawable.cheese, "치즈피자","5,000원","평점 3.4 점"));
        data.add(new MyItem(R.drawable.pepper, "페퍼로니피자","5,000원","평점 3.0 점"));
        data.add(new MyItem(R.drawable.spotato, "고구마피자","6,000원","평점 4.5 점"));
        data.add(new MyItem(R.drawable.spotato, "직화파인애플피자","8,000원","평점 3.9 점"));

        adapter = new MyAdapter(this, R.layout.item ,data);

        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

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