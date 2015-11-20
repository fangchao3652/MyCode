package com.lnu.fang.pic_;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lnu.fang.pic_.bean.FolderBean;
import com.lnu.fang.pic_.utils.CommonAdapter;
import com.lnu.fang.pic_.utils.ViewHolder;

import java.util.List;

/**
 * Created by Fang on 2015/11/20.
 */
public class ListImageDirPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertVeiw;
    private ListView mlistView;

    private Context context;
    private List<FolderBean> mDatas;

    public interface onDirSelectedListener {
        void onSelected(FolderBean folderBean);
    }

    private onDirSelectedListener mOnDirSelectedListener;

    public void setmOnDirSelectedListener(onDirSelectedListener mOnDirSelectedListener) {
        this.mOnDirSelectedListener = mOnDirSelectedListener;
    }

    public ListImageDirPopupWindow(Context context, List<FolderBean> mDatas) {
        calculateWidthAndHeight(context);
        mConvertVeiw = LayoutInflater.from(context).inflate(R.layout.popwindow_main, null);
        this.mDatas = mDatas;
        this.context = context;

        setContentView(mConvertVeiw);
      //  Log.e("wwwwwwwwww",ViewGroup.LayoutParams.MATCH_PARENT+"");
        setWidth(mWidth);
        setHeight(mHeight);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
/**
 * 如果有背景，则会在contentView外面包一层PopupViewContainer之后作为mPopupView，如果没有背景，则直接用contentView作为mPopupView。

 　　而这个PopupViewContainer是一个内部私有类，它继承了FrameLayout，在其中重写了Key和Touch事件的分发处理：
 由于PopupView本身并没有重写Key和Touch事件的处理，所以如果没有包这个外层容器类，点击Back键或者外部区域是不会导致弹框消失的。

 设置了PopupWindow的background,点击Back键或者点击弹窗的外部区域,弹窗就会dismiss.
 　　相反,如果不设置PopupWindow的background,那么点击back键和点击弹窗的外部区域,弹窗是不会消失的.
 */
        setBackgroundDrawable(new BitmapDrawable());

     /*   setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
*/
        initView();
        initEvent();
    }


    private void initView() {
        mlistView = (ListView) mConvertVeiw.findViewById(R.id.id_list_dir);
        mlistView.setAdapter(new ListDIrAdapter(context, mDatas, R.layout.list_dir_item));
    }

    private void initEvent() {
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mOnDirSelectedListener != null) {
                    mOnDirSelectedListener.onSelected(mDatas.get(position));
                }
            }
        });
    }

    private void calculateWidthAndHeight(Context context) {
//        int mWidth = context.getResources().getDisplayMetrics().widthPixels;
//        int mHeight = context.getResources().getDisplayMetrics().heightPixels;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
          mWidth = outMetrics.widthPixels;
          mHeight = (int) (outMetrics.heightPixels * 0.6);
    }

    private class ListDIrAdapter extends CommonAdapter<FolderBean> {

        public ListDIrAdapter(Context context, List<FolderBean> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, FolderBean folderBean) {
            holder.setTextViewText(R.id.id_dir_item_name, folderBean.getName());
            holder.setImageByUrl(R.id.id_dir_item_image,
                    folderBean.getFirstImagePath());
            holder.setTextViewText(R.id.id_dir_item_count, folderBean.getCount() + "张");
        }
    }
}
