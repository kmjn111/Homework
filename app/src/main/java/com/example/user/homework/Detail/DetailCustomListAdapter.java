package com.example.user.homework.Detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.homework.R;

import java.util.ArrayList;

/**
 * Created by user on 2017-10-15.
 */

public class DetailCustomListAdapter extends BaseAdapter {
    private Context mContext;
    private int mResource;
    private ArrayList<DetailCustomItem> mItems = new ArrayList<DetailCustomItem>();

    public DetailCustomListAdapter(Context context, int resource, ArrayList<DetailCustomItem> items){
        mContext = context;
        mItems = items;
        mResource = resource;
    }

    public int getCount(){
        return mItems.size();
    }

    public Object getItem(int position){
        return mItems.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent,false);
        }
        // Set Icon
        ImageView icon = (ImageView) convertView.findViewById(R.id.iconitem);
        icon.setImageResource(mItems.get(position).mIcon);

        // Set Text 01
        TextView name = (TextView) convertView.findViewById(R.id.textitem1);
        name.setText(mItems.get(position).nName);

        // Set Text 02
        TextView age = (TextView) convertView.findViewById(R.id.textitem2);
        age.setText(mItems.get(position).nAge);

        return convertView;

    }
}


