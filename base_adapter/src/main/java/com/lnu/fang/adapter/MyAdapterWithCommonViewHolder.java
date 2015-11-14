package com.lnu.fang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lnu.fang.activity.R;
import com.lnu.fang.bean.Bean;
import com.lnu.fang.utils.CommonAdapter;
import com.lnu.fang.utils.ViewHolder;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fang on 2015/11/14.
 */
public class MyAdapterWithCommonViewHolder extends CommonAdapter<Bean> {

    private List<Integer> mPos = new ArrayList<>();//记录选中
int LayoutId;
    public MyAdapterWithCommonViewHolder(Context context, List datas,int layoutId) {
        super(context, datas,layoutId);
    }

    @Override
    public void convert(final ViewHolder holder, Bean bean) {
        holder.setTextViewText(R.id.id_title, bean.getTitle())
                .setTextViewText(R.id.id_desc, bean.getDesc())
                .setTextViewText(R.id.id_time, bean.getTime())
                .setTextViewText(R.id.id_phone, bean.getPhone());
        final CheckBox cb = holder.getView(R.id.id_cb);
        cb.setChecked(false);

        if(mPos.contains(holder.getmPosition())){
            cb.setChecked(true);
        }
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb.isChecked()) {
                    mPos.add(holder.getmPosition());
                }else{
                    mPos.remove((Integer)holder.getmPosition());//这里用的remove object
                }
            }
        });
     /*
        TextView mDesc = holder.getView(R.id.id_desc);
        mDesc.setText(bean.getDesc());
        TextView mTime = holder.getView(R.id.id_time);
        mTime.setText(bean.getTime());
        TextView mPhone = holder.getView(R.id.id_phone);
        mPhone.setText(bean.getPhone());*/
    }
}
