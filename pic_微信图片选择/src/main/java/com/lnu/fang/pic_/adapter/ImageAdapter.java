package com.lnu.fang.pic_.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

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
     * 用户选择的图片，存储为图片的完整路径(切换到别的路径后 依然共享 这些选中的的图片)
     */
    public static List<String> mSelectedImage = new LinkedList<String>();

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
    public void convert(ViewHolder helper, final String filename) {
        //设置no_pic
         helper.setImageResource(R.id.item_image, R.drawable.pictures_no);
        //设置no_selected
        helper.setImageResource(R.id.item_select,
                R.drawable.picture_unselected);
        //设置图片
        helper.setImageByUrl(R.id.item_image, mDirPath + "/" + filename);


        final ImageView mImageView = helper.getView(R.id.item_image);
        final ImageView mSelect = helper.getView(R.id.item_select);

        mImageView.setColorFilter(null);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = mDirPath + "/" + filename;
                if (mSelectedImage.contains(filePath)) {
                    mSelectedImage.remove(filePath);
                    mSelect.setImageResource(R.drawable.picture_unselected);
                    mImageView.setColorFilter(null);
                } else {
                    mSelectedImage.add(filePath);
                    mSelect.setImageResource(R.drawable.pictures_selected);
                    mImageView.setColorFilter(Color.parseColor("#77000000"));
                }
                //notifyDataSetChanged();//容易闪屏

            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + filename)) {
            mSelect.setImageResource(R.drawable.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }
}