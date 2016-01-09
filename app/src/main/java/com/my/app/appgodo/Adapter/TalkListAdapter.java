package com.my.app.appgodo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my.app.appgodo.Dto.PushDto;
import com.my.app.appgodo.R;

import java.util.ArrayList;

/**
 * Created by Mark on 2015-12-23.
 */
public class TalkListAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<PushDto> mListData = new ArrayList<PushDto>();

    public TalkListAdapter(Context context, ArrayList<PushDto> mListData){
        super();
        this.mContext = context;
        this.mListData = mListData;
    }
    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
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
            convertView = inflater.inflate(R.layout.item_talk, null);

            mHolder.mTalkView = (TextView) convertView.findViewById(R.id.talkView);
            mHolder.mRegView = (TextView) convertView.findViewById(R.id.regView);
            convertView.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) convertView.getTag();
        }
        PushDto mPushdto = mListData.get(position);
        Log.d("talk", mPushdto.getOrdno() + "_" + mPushdto.getPtype());
        mHolder.mTalkView.setText(mPushdto.getMessage());
        mHolder.mRegView.setText(mPushdto.getRegdt());

        return convertView;
    }

    private class ViewHolder{
        public TextView mTalkView;
        public TextView mRegView;
    }
}
