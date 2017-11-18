package com.example.user.homework.detailfragment;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailMainFragment extends Fragment {

    public static String parentId = "";
    
    DBHelper mDbHelper;
    DBDetailHelper mDbDetailHelper;

    DetailCustomListAdapter adapter;
    ArrayList<DetailCustomItem> data = new ArrayList<DetailCustomItem>();

    TextView textview;
    TextView name, addr, call_number;
    ImageView imageView;

    int mCurCheckPosition = -1;

    public interface OnTitleSelectedListener {
        public void onTitleSelected(String id);
    }

    public DetailMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = (View)inflater.inflate(R.layout.fragment_detail_main, container, false);
        
        mDbHelper= new DBHelper(rootView.getContext());
        mDbDetailHelper= new DBDetailHelper(rootView.getContext());

        viewData(rootView, parentId);
        viewAllToListView(rootView);

        textview = (TextView) rootView.findViewById(R.id.call_number);

        //전화하기 버튼 연결 및 클릭 시 전화하기 기능 연결
        ImageButton Imgbtn = (ImageButton) rootView.findViewById(R.id.imgbtn);
        Imgbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent implicit_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+textview.getText()));
                startActivity(implicit_intent);
            }

        });

        return rootView;
    }

    private void viewData(View rootView, String id) {

        name = (TextView)rootView.findViewById(R.id.name);
        addr = (TextView)rootView.findViewById(R.id.addr);
        call_number = (TextView)rootView.findViewById(R.id.call_number);
        imageView = (ImageView)rootView.findViewById(R.id.image_view);
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

    private void viewAllToListView(View rootView) {

        Cursor cursor = mDbDetailHelper.getAllHomeworksBySQL(parentId);
        while (cursor.moveToNext()) {
            data.add(new DetailCustomItem(cursor.getString(0), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
        }
        Toast.makeText(rootView.getContext(),"Record "+data.size(), Toast.LENGTH_SHORT).show();

        //어댑터를 통하여 리스트뷰에 데이터 넣기
        adapter = new DetailCustomListAdapter(rootView.getContext(), R.layout.item, data);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        //메뉴를 눌렀을 때 DetailSubActivity 로 보내줄 데이터 셋팅 및 DetailSubActivity 열기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mCurCheckPosition = position;
                Activity activity = getActivity();
                ((OnTitleSelectedListener)activity).onTitleSelected(data.get(mCurCheckPosition)._id);
                /*Intent intent = new Intent(getContext().getApplicationContext(), DetailSubActivity.class);

                intent.putExtra("title", data.get(position).nName);
                intent.putExtra("path", data.get(position).imagePath);
                intent.putExtra("price", data.get(position).nPrice);
                intent.putExtra("explain", data.get(position).nValue);

                startActivity(intent);*/
            }
        });
    }
    
    
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt("curChoice", -1);
            if (mCurCheckPosition >= 0) {
                Activity activity = getActivity(); // activity associated with the current fragment
                ((OnTitleSelectedListener)activity).onTitleSelected(data.get(mCurCheckPosition)._id);

                ListView lv = (ListView) getView().findViewById(R.id.list_view);
                lv.setSelection(mCurCheckPosition);
                lv.smoothScrollToPosition(mCurCheckPosition);
            }
        }
    }

//    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

}
