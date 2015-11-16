package com.lnu.fang.imooc_zhuanpan;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {
    private LuckPanSV luckPanSV;
    private ImageView startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        luckPanSV = (LuckPanSV) findViewById(R.id.id_luckypan);
        startBtn = (ImageView) findViewById(R.id.id_startbtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!luckPanSV.isstart()) {
//                    luckPanSV.luckStart();
                    luckPanSV.luckStart(1);
                    startBtn.setImageResource(R.drawable.stop);
                } else {
                    if (!luckPanSV.isShouldEnd()) {
                        luckPanSV.luckStop();
                        startBtn.setImageResource(R.drawable.start);

                    } else {
                        //什么也不做
                    }
                }
            }
        });
    }


}
