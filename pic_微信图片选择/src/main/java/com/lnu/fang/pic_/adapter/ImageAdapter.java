package com.lnu.fang.pic_.adapter;

import android.content.Context;

import com.lnu.fang.pic_.R;
import com.lnu.fang.pic_.utils.CommonAdapter;
import com.lnu.fang.pic_.utils.ViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Fang on 2015/11/19.
 */
public class ImageAdapter extends CommonAdapter<String> {

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public List<String> mSelectedImage = new LinkedList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;

    public ImageAdapter(Context context, List<String> mDatas, int itemLayoutId,
                        String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
    }

    @Override
    public void convert(ViewHolder helper, String filename) {
        //设置no_pic
        helper.setImageResource(R.id.item_image, R.drawable.pictures_no);
        //设置no_selected
        helper.setImageResource(R.id.item_select,
                R.drawable.picture_unselected);
        //设置图片
        helper.setImageByUrl(R.id.item_image, mDirPath + "/" + filename);
    }
}