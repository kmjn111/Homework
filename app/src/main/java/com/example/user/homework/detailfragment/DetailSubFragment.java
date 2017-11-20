package com.example.user.homework.detailfragment;



import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.homework.R;
import com.example.user.homework.data.DBDetailHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailSubFragment extends Fragment {

    static int index=-1;
    static String subId;
    DBDetailHelper mDbDetailHelper;

    public DetailSubFragment() {
        // Required empty public constructor
    }

    public void setSelection(int i) { index = i; }
    public void setSubId(String _id){
        subId = _id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_sub, container, false);
        TextView title = (TextView)view.findViewById(R.id.textView1);
        TextView price = (TextView)view.findViewById(R.id.textView2);
        TextView value = (TextView)view.findViewById(R.id.textView3);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView1);

        mDbDetailHelper= new DBDetailHelper(view.getContext());
        Cursor cursor = mDbDetailHelper.getSelectHomeworksBySQL(DetailMainFragment.parentId, subId);
        while (cursor.moveToNext()) {
            //Toast.makeText(this,"Record Inserted"+id+","+cursor.getString(0)+","+cursor.getString(1)+","+cursor.getString(2)+","+cursor.getString(3), Toast.LENGTH_SHORT).show();
            title.setText(cursor.getString(3));
            price.setText(cursor.getString(4));
            value.setText(cursor.getString(5));
            String imagePath=cursor.getString(2);
            if(imagePath != null && !"".equals(imagePath)){
                Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                imageView.setImageBitmap(myBitmap);
            }
        }




        return view;
    }

}
