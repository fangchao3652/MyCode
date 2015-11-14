package com.lnu.fang.utils;

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
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> mdatas;

    protected LayoutInflater inflater;

    public CommonAdapter(Context context, List<T> datas) {
        this.context = context;
        this.mdatas = datas;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mdatas.size();
    }

    @Override
    public T getItem(int i) {
        return mdatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View parent, ViewGroup viewGroup) {
        ViewHolder holder = ViewHolder.get(context, parent, viewGroup, R.layout.item_listview, position);

        convert(holder, getItem(position));

        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);
}
