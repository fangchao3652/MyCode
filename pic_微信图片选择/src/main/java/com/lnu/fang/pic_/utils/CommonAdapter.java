package com.lnu.fang.pic_.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Fang on 2015/11/14.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> mdatas;
    protected int layoutId;
    protected LayoutInflater inflater;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.context = context;
        this.mdatas = datas;
        this.layoutId = layoutId;
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
        ViewHolder holder = ViewHolder.get(context, parent, viewGroup, layoutId, position);

        convert(holder, getItem(position));

        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);
}
