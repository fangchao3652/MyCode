package com.lnu.fang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fang on 2015/11/14.
 */
public class FlowLayout extends ViewGroup {
    /**
     * new的时候
     *
     * @param context
     */
    public FlowLayout(Context context) {
        this(context, null);
    }

    /**
     * xml 使用自带属性 而没有使用自定义属性的时候
     *
     * @param context
     * @param attrs
     */
    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 使用了自定义属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //  Log.e("fac",sizeWidth+"");
        //wrap content
        int width = 0;//wrap content时的宽度 自己计算
        int height = 0;//wrap content时的高度 自己计算
        //记录每一行的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //子view的宽和高
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
            if (lineWidth + childWidth > sizeWidth-getPaddingLeft()-getPaddingRight()) {
                width = Math.max(lineWidth, width);
                lineWidth = childWidth;//重置
                height += lineHeight;
                lineHeight = childHeight;//重置
            } else {//未换行
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == count - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }

        }

        setMeasuredDimension(modeWidth == MeasureSpec.AT_MOST ? width+getPaddingLeft()+getPaddingRight() : sizeWidth,

                modeHeight == MeasureSpec.AT_MOST ? height+getPaddingTop()+getPaddingBottom() : sizeHeight);
    }

    /**
     * 存储所有的view
     */
    List<List<View>> mAllViews = new ArrayList<>();
    List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        //当前viewGroup的宽度
        int widh = getWidth();
        int lineW = 0;
        int lineH = 0;
        List<View> lineview = new ArrayList<>();
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childw = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childH = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (childw + lineW > widh-getPaddingLeft()-getPaddingRight()) { //如果需要换行
                mLineHeight.add(lineH);
                mAllViews.add(lineview);
                //重置
                lineW = childw;
                lineH = childH;
                lineview = new ArrayList<>();
                lineview.add(child);
            } else {
                lineW += childw;
                lineH = Math.max(lineH, childH);
                lineview.add(child);
            }
        }//for  end
        //最后一行 处理
        mLineHeight.add(lineH);
        mAllViews.add(lineview);

        //设置 子view的位置
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int lineNum = mAllViews.size();
        for (int i = 0; i < lineNum; i++) {
            lineview = mAllViews.get(i);
            int lineHeight = mLineHeight.get(i);
            for (int j = 0; j < lineview.size(); j++) {
                View child = lineview.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            }
            //left = 0;
          left = getPaddingLeft();
            top += lineHeight;

        }

    }

    /**
     * 与当前viewgroup 对应的layoutparams
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}






















