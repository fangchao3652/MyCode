package com.lnu.fang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnu.fang.activity.R;
import com.lnu.fang.bean.Bean;

import java.util.List;

/**
 * Created by Fang on 2015/11/14.
 */
public class MyAdapter extends BaseAdapter {
    public MyAdapter(Context context, List<Bean> mdatas) {
        this.inflater = LayoutInflater.from(context);
        this.mdatas = mdatas;
    }

    private LayoutInflater inflater;
    private List<Bean> mdatas;

    @Override
    public int getCount() {
        return mdatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mdatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_listview, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.mtitle = (TextView) view.findViewById(R.id.id_title);
            viewHolder.mDesc = (TextView) view.findViewById(R.id.id_desc);
            viewHolder.mPhone = (TextView) view.findViewById(R.id.id_phone);
            viewHolder.mTime = (TextView) view.findViewById(R.id.id_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Bean bean = mdatas.get(i);
        viewHolder.mTime.setText(bean.getTime());
        viewHolder.mtitle.setText(bean.getTitle());
        viewHolder.mPhone.setText(bean.getPhone());
        viewHolder.mDesc.setText(bean.getDesc());

        return view;
    }

    private class ViewHolder {
        TextView mtitle;
        TextView mDesc;
        TextView mTime;
        TextView mPhone;
    }
}
