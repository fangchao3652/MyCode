package com.lnu.fang.pagerviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private ViewPager mViewPager;
    private int[] mImgIds = new int[]{R.drawable.ipad,
            R.drawable.iphone, R.drawable.danfan};
    private List<ImageView> mImageViews = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //向zuo'hua
        /*/41996560  0    -1
        {419d7b30    1    0*/
        setContentView(R.layout.activity_main);

        initData();

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setPageTransformer(true, new RotateDownPageTransformer());
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(mImageViews.get(position));
                return mImageViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {

                container.removeView(mImageViews.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return mImgIds.length;
            }
        });

    }

    private void initData() {
        for (int imgId : mImgIds) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            mImageViews.add(imageView);
        }
    }

    /**
     * 潜藏型
     */
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) { // [-∞ ,-1)
                // 这一页已经是最左边的屏幕页
                view.setAlpha(0);
            } else if (position <= 0) { // [-1,0]
                    // 向左面滑屏使用默认的过渡动画   a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0

                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) { // (0,1]
                // 淡出页面
                view.setAlpha(1 - position);
                // 抵消默认的整页过渡
                view.setTranslationX(pageWidth * -position);
                // 根据缩放系数变化 (在 MIN_SCALE 和 1 之间变化)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else { // (1,+∞]
                // 这一页已经是最右边的屏幕页
                view.setAlpha(0);
            }
        }
    }

    /**
     * 缩放型
     */
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            Log.e("pager position--- -", view.toString() + "====" + position + "");
            if (position < -1) { // [-∞,-1)
            // 这一页已经是最左边的屏幕页
                view.setAlpha(0);
            } else if (position <= 1) { // [-1,1]
            // 修改默认的滑动过渡效果为缩放效果
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                // 开始根据缩放系数进行变化 (在 MIN_SCALE 和 1 之间变化)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // 根据大小（缩放系数）变化化透明度实现淡化页面效果
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+∞ ]
                    // 这一页已经是最右边的屏幕页
                view.setAlpha(0);
            }
        }
    }
    public class RotateDownPageTransformer implements ViewPager.PageTransformer
    {

        private static final float ROT_MAX = 20.0f;
        private float mRot;



        public void transformPage(View view, float position)
        {

            Log.e("TAG", view + " , " + position + "");

            if (position < -1)
            { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setRotation(0);


            } else if (position <= 1) // a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0
            { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                if (position < 0)
                {


                    mRot = (ROT_MAX * position);

                    view.setPivotX(   view.getMeasuredWidth()*0.5f );
                    view.setPivotY(view.getMeasuredHeight());
                    view.setRotation(mRot);
                } else
                {

                    mRot = (ROT_MAX * position);

                    view.setPivotX(   view.getMeasuredWidth()*0.5f );
                    view.setPivotY(view.getMeasuredHeight());
                    view.setRotation(mRot);
                }

                // Scale the page down (between MIN_SCALE and 1)

                // Fade the page relative to its size.

            } else
            { // (1,+Infinity]
                // This page is way off-screen to the right.

                view.setRotation(0);
            }
        }
    }
}