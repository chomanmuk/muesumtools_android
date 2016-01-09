package com.my.app.appgodo.Adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my.app.appgodo.Dto.PushDto;
import com.my.app.appgodo.R;

import java.util.ArrayList;

/**
 * Created by Mark on 2015-12-24.
 */
public class SpinnerAdapter extends BaseAdapter {
    private Context mContext = null;
    private String[] mListData = null;

    public SpinnerAdapter(Context context, String[] mListData){
        super();
        this.mContext = context;
        this.mListData = mListData;
    }

    @Override
    public int getCount() {
        return mListData.length;
    }

    @Override
    public Object getItem(int position) {
        return mListData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if(convertView == null){
            mHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_dropdown_item, null);
            mHolder.spanerItem = (TextView) convertView.findViewById(R.id.spanerItem);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        if(position%2 == 0) {
            mHolder.spanerItem.setBackgroundResource(R.color.tr2);
        }else{
            mHolder.spanerItem.setBackgroundResource(R.color.tr1);
        }
        mHolder.spanerItem.setText(mListData[position]);
        mHolder.spanerItem.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics()));

        return convertView;
    }
    private class ViewHolder{
        public TextView spanerItem;

    }
}
