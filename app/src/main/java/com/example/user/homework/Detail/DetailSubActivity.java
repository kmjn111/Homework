package com.example.user.homework.Detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.homework.R;

/**
 * Created by user on 2017-10-15.
 */

public class DetailSubActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cheese);

        TextView Title = (TextView)findViewById(R.id.textView1);
        TextView Price = (TextView)findViewById(R.id.textView2);
        TextView Value = (TextView)findViewById(R.id.textView3);
        ImageView img = (ImageView)findViewById(R.id.imageView1);

        //MainActivity에서 받아온 정보 셋팅
        Intent intent = getIntent();
        Title.setText(intent.getStringExtra("title"));
        Price.setText(intent.getStringExtra("price"));
        Value.setText(intent.getStringExtra("value"));
        img.setImageResource(intent.getIntExtra("img",0));
    }
}
