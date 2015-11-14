package com.lnu.fang.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lnu.fang.view.FlowLayout;


public class MainActivity extends ActionBarActivity {
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
        for (int i = 0; i < mVals.length; i++) {
            TextView btn = new TextView(this);
            btn.setBackgroundColor(Color.argb(250,200,120,211));
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
           lp.setMargins(4,4,4,4);
            btn.setText(mVals[i]);
            mFlowLayout.addView(btn,lp);
        }
    }
}
