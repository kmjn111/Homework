package com.example.user.homework.detailfragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.user.homework.R;

public class DetailSubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_sub);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        Intent intent = getIntent();
        String subId = intent.getStringExtra("_id");

        DetailSubFragment details = new DetailSubFragment();
        details.setSubId(subId);
        getSupportFragmentManager().beginTransaction().replace(R.id.details, details).commit();
    }
}
