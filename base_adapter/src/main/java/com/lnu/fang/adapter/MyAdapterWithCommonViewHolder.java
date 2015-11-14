package com.lnu.fang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnu.fang.activity.R;
import com.lnu.fang.bean.Bean;
import com.lnu.fang.utils.CommonAdapter;
import com.lnu.fang.utils.ViewHolder;

import java.security.PublicKey;
import java.util.List;

/**
 * Created by Fang on 2015/11/14.
 */
public class MyAdapterWithCommonViewHolder extends CommonAdapter<Bean> {
    public  MyAdapterWithCommonViewHolder(Context context,List  datas){
        super(context,datas);
    }
    @Override
    public void convert(ViewHolder holder, Bean bean) {
        TextView mTitle = holder.getView(R.id.id_title);
        mTitle.setText(bean.getTitle());
        TextView mDesc = holder.getView(R.id.id_desc);
        mDesc.setText(bean.getDesc());
        TextView mTime = holder.getView(R.id.id_time);
        mTime.setText(bean.getTime());
        TextView mPhone = holder.getView(R.id.id_phone);
        mPhone.setText(bean.getPhone());
    }
}
