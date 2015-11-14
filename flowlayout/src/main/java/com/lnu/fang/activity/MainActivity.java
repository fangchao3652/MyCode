package com.lnu.fang.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lnu.fang.view.FlowLayout;


public class MainActivity extends Activity {
    private String[] mVals = new String[]{"Helloa 哈哈", "fangchao 1", "zhl我爱你", "你好", "this ", "Hello", "fang", "lnu", "你好", "this is my code", "Hello", "chao", "zhao", "你好", "this is my love", "Hello", "fangchao", "红蕾", "你好", "this is my love"};
    private FlowLayout mFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout = (FlowLayout) findViewById(R.id.fl);
        initData();
    }

    public void initData() {
       /* for (int i = 0; i < mVals.length; i++) {
            TextView btn = new TextView(this);
            btn.setBackgroundColor(Color.argb(250, 200, 120, 211));
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(4, 4, 4, 4);
            btn.setText(mVals[i]);
            mFlowLayout.addView(btn, lp);
        }*/

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < mVals.length; i++) {
            /**inflate()方法一般接收两个参数，第一个参数就是要加载的布局id，第二个参数是指给该布局的外部再嵌套一层父布局，如果不需要就直接传null
             * true：并且root存在，将xml挂载到root下，返回root
             false：返回xml的跟布局
             */
            TextView tv = (TextView) inflater.inflate(R.layout.tv, mFlowLayout, false);
            tv.setText(mVals[i]);
            mFlowLayout.addView(tv);
        }
    }
}
