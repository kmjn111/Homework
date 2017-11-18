package com.example.user.homework.Detail;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.homework.R;
import com.example.user.homework.data.DBDetailHelper;
import com.example.user.homework.data.DBHelper;

import java.util.ArrayList;

public class DetailActivity extends ActionBarActivity {
    DBHelper mDbHelper;
    DBDetailHelper mDbDetailHelper;

    DetailCustomListAdapter adapter;
    ArrayList<DetailCustomItem> data = new ArrayList<DetailCustomItem>();

    TextView textview;
    TextView name, addr, call_number;
    ImageView imageView;

    String parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //MainActivity에서 받아온 정보 셋팅
        Intent intent = getIntent();
        parentId = intent.getStringExtra("_id");
        mDbHelper= new DBHelper(this);
        mDbDetailHelper= new DBDetailHelper(this);

        viewData(parentId);
        viewAllToListView();

        textview = (TextView) findViewById(R.id.call_number);

        //전화하기 버튼 연결 및 클릭 시 전화하기 기능 연결
        ImageButton Imgbtn = (ImageButton) findViewById(R.id.imgbtn);
        Imgbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent implicit_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+textview.getText()));
                startActivity(implicit_intent);
            }

        });

    }

    private void viewData(String id) {

        name = (TextView)findViewById(R.id.name);
        addr = (TextView)findViewById(R.id.addr);
        call_number = (TextView)findViewById(R.id.call_number);
        imageView = (ImageView)findViewById(R.id.image_view);
        //Toast.makeText(this,"Record "+id, Toast.LENGTH_SHORT).show();
        Cursor cursor = mDbHelper.getSelectHomeworksBySQL(id);
        while (cursor.moveToNext()) {
            //Toast.makeText(this,"Record Inserted"+id+","+cursor.getString(0)+","+cursor.getString(1)+","+cursor.getString(2)+","+cursor.getString(3), Toast.LENGTH_SHORT).show();
            name.setText(cursor.getString(1));
            addr.setText(cursor.getString(2));
            call_number.setText(cursor.getString(3));
            String imagePath=cursor.getString(4);
            if(imagePath != null && !"".equals(imagePath)){
                Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                imageView.setImageBitmap(myBitmap);
            }
        }

     }

    private void viewAllToListView() {

        Cursor cursor = mDbDetailHelper.getAllHomeworksBySQL(parentId);
        while (cursor.moveToNext()) {
            data.add(new DetailCustomItem(cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5)));
        }

        //어댑터를 통하여 리스트뷰에 데이터 넣기
        adapter = new DetailCustomListAdapter(this, R.layout.item ,data);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        //메뉴를 눌렀을 때 DetailSubActivity 로 보내줄 데이터 셋팅 및 DetailSubActivity 열기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getApplicationContext(), DetailSubActivity.class);

                intent.putExtra("title", data.get(position).nName);
                intent.putExtra("path", data.get(position).imagePath);
                intent.putExtra("price", data.get(position).nPrice);
                intent.putExtra("explain", data.get(position).nValue);

                startActivity(intent);
            }
        });

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

