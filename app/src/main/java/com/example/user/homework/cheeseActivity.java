package com.example.user.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2017-10-15.
 */

public class cheeseActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cheese);

        TextView Title = (TextView)findViewById(R.id.textView1);
        TextView Price = (TextView)findViewById(R.id.textView2);
        TextView Value = (TextView)findViewById(R.id.textView3);
        ImageView img = (ImageView)findViewById(R.id.imageView1);

        Intent intent = getIntent();
        Title.setText(intent.getStringExtra("title"));
        Price.setText(intent.getStringExtra("price"));
        Value.setText(intent.getStringExtra("value"));
        img.setImageResource(intent.getIntExtra("img",0));
    }
}
