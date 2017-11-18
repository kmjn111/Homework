package com.example.user.homework.detailfragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.homework.R;

public class DetailMainActivity extends AppCompatActivity implements DetailMainFragment.OnTitleSelectedListener{

    public String parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_main);

    }

    public void onTitleSelected(String id) {
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            DetailSubFragment detailsFragment = new DetailSubFragment();
            Toast.makeText(getApplicationContext(), "selected Data::"+id, Toast.LENGTH_SHORT).show();
            detailsFragment.setSubId(id);
            getSupportFragmentManager().beginTransaction().replace(R.id.details, detailsFragment).commit();
        } else {
            Intent intent = new Intent(this, DetailSubActivity.class);
            intent.putExtra("_id", id);
            startActivity(intent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //ActionBar 메뉴 클릭에 대한 이벤트 처리

        int id = item.getItemId();
        switch (id){
            case R.id.action_add:

                //상세화면으로 이동
                Intent intent = new Intent(getApplicationContext(), DetailSubRegisterActivity.class);
                intent.putExtra("_id", parentId);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}