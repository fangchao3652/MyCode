package com.lnu.fang.pic_;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lnu.fang.pic_.adapter.ImageAdapter;
import com.lnu.fang.pic_.bean.FolderBean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity {
    private GridView mGirdView;
    private ImageAdapter imageAdapter;
    private List<String> mImags;//所有图片的 filename


    private RelativeLayout mBottomLy;
    private TextView mDirName, mDirCount;

    private File mCurrentDir;
    private int mMaxCount;

    private List<FolderBean> mFolderBeans = new ArrayList<>();


    private ProgressDialog mProgressDialog;

    private ListImageDirPopupWindow popupWindow;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mProgressDialog.dismiss();
                data2View();

                initDirPopupWindow();
            }
        }
    };

    private void initDirPopupWindow() {
        popupWindow = new ListImageDirPopupWindow(this, mFolderBeans);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Light();
            }
        });

        popupWindow.setmOnDirSelectedListener(new ListImageDirPopupWindow.onDirSelectedListener() {
            @Override
            public void onSelected(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getDir());
                mImags = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")) {
                            return true;
                        }
                        return false;
                    }
                }));

                //Collections.reverse(mImags);
                imageAdapter = new ImageAdapter(MainActivity.this, mImags, R.layout.grid_item, mCurrentDir.getAbsolutePath());
                mGirdView.setAdapter(imageAdapter);

                mDirName.setText(mCurrentDir.getName());
                mDirCount.setText("" + mImags.size());
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 内容区域变亮
     */
    private void Light() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * // 设置背景颜色变暗
     */
    private void lightOff() {

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = .3f;
        getWindow().setAttributes(lp);
    }

    private void data2View() {
        if (mCurrentDir == null) {
            Toast.makeText(getApplicationContext(), "啊哦，一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mImags = Arrays.asList(mCurrentDir.list());
       //Collections.reverse(mImags);
        imageAdapter = new ImageAdapter(MainActivity.this, mImags, R.layout.grid_item, mCurrentDir.getAbsolutePath());
        mGirdView.setAdapter(imageAdapter);

        mDirCount.setText(mMaxCount + "");
        mDirName.setText(mCurrentDir.getName());
        //mDirName.setText(mCurrentDir.getAbsolutePath());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDatas();
        initEvent();
    }

    /**
     * 利用contentProvider扫描手机中的所有图片
     */
    private void initDatas() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog = ProgressDialog.show(this, null, "正在加载中...");
        new Thread() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = MainActivity.this.getContentResolver();
                // 只查询jpeg和png的图片
                Cursor c = cr.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED );

                Log.e("TAG", c.getCount() + "");

                Set<String> mSetParentPaths = new HashSet<String>();
                while (c.moveToNext()) {//停在第一列的上面
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;//有的确实找不到父路径
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;
                    if (mSetParentPaths.contains(dirPath)) {//防止重复扫描
                        continue;//当前文件价扫描过了
                    } else {
                        mSetParentPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImagePath(path);

                    }
                    if (parentFile.list() == null) {
                        continue;//防止程序挂掉 有的图没有parent  但却能查出来
                    }
                    int picSize = parentFile.list(new FilenameFilter() {//过滤掉不是图片的文件
                        @Override
                        public boolean accept(File file, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")) {
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    mFolderBeans.add(folderBean);
                    if (picSize > mMaxCount) {
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }
                c.close();
                mHandler.sendEmptyMessage(1);//通知handler 扫描图片完成
                // mSetParentPaths=null;
            }
        }.start();
    }


    /**
     * 初始化View
     */
    private void initView() {
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mDirName = (TextView) findViewById(R.id.id_dir_name);
        mDirCount = (TextView) findViewById(R.id.id_dir_count);

        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);

    }

    private void initEvent() {
        /**
         *  为底部的布局设置点击事件，弹出popupWindow*/

        mBottomLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popupwindow 动画
                popupWindow
                .setAnimationStyle(R.style.anim_popup_dir);
              popupWindow.showAsDropDown(mBottomLy, 0, 0);
                //   popupWindow.showAtLocation(mBottomLy, Gravity.TOP, 0, 0);
                lightOff();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }
}
