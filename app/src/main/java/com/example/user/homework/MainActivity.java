package com.example.user.homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //주소 검색으로 시작
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);

    }
}
