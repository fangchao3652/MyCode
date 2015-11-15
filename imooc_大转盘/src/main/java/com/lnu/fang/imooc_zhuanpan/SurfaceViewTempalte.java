package com.lnu.fang.imooc_zhuanpan;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Fang on 2015/11/15.
 */
public class SurfaceViewTempalte extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Canvas canvas;
    //用于绘制 线程
    private Thread t;
    /**
     * 线程的控制开关
     */
    private boolean isRunning;


    public SurfaceViewTempalte(Context context) {
        this(context, null);
    }

    public SurfaceViewTempalte(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);

        //可获取焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
//设置常量
        setKeepScreenOn(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //开启子线程
        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;

    }

    @Override
    public void run() {
        while (isRunning) {
            draw();
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                //绘制。。。

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(canvas!=null){
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
