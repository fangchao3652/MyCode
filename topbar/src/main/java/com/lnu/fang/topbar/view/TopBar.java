package com.lnu.fang.topbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lnu.fang.topbar.R;


/**
 * Created by Fang on 2015/11/15.
 */
public class TopBar extends RelativeLayout {
    private Button leftBtn, rightBtn;
    private TextView tvTitle;

    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private float titleTextSize;
    private int titleTextColor;
    private String title;

    private LayoutParams leftParams;
    private LayoutParams rightParams;
    private LayoutParams titleParams;


    private topBarClickListener topBarClickListener;

    public interface topBarClickListener {
        public void leftClick();

        public void rightClick();
    }

    public void setOnTopBarClickListener(topBarClickListener listener) {
        this.topBarClickListener = listener;
    }

    public TopBar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);
        leftTextColor = ta.getColor(R.styleable.Topbar_leftTextColor, 0);
        leftBackground = ta.getDrawable(R.styleable.Topbar_leftBackground);
        leftText = ta.getString(R.styleable.Topbar_leftText);

        rightTextColor = ta.getColor(R.styleable.Topbar_rightTextColor, 0);
        rightBackground = ta.getDrawable(R.styleable.Topbar_rightBackground);
        rightText = ta.getString(R.styleable.Topbar_rightText);

        titleTextSize = ta.getDimension(R.styleable.Topbar_mytitleTextSize, 12);
        titleTextColor = ta.getColor(R.styleable.Topbar_mytitleTextColor, 0);
        title = ta.getString(R.styleable.Topbar_mytitle);


        ta.recycle();

        leftBtn = new Button(context);
        rightBtn = new Button(context);
        tvTitle = new TextView(context);


        leftBtn.setTextColor(leftTextColor);
        leftBtn.setText(leftText);
        leftBtn.setBackground(leftBackground);

        rightBtn.setTextColor(rightTextColor);
        rightBtn.setText(rightText);
        rightBtn.setBackground(rightBackground);

        tvTitle.setText(title);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setGravity(Gravity.CENTER);


        setBackgroundColor(0xFFF65266);
//左边
        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(ALIGN_PARENT_LEFT, TRUE);//居左对齐
        addView(leftBtn, leftParams);
//右边
        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(ALIGN_PARENT_RIGHT, TRUE);//居右对齐
        addView(rightBtn, rightParams);


        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(CENTER_IN_PARENT, TRUE);//居右对齐
        addView(tvTitle, titleParams);


        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                topBarClickListener.leftClick();
            }
        });
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                topBarClickListener.rightClick();
            }
        });
    }

    public void setLeftVisible(boolean b) {
        if (b) {
            leftBtn.setVisibility(View.VISIBLE);
        } else
            leftBtn.setVisibility(View.GONE);
    }

}
