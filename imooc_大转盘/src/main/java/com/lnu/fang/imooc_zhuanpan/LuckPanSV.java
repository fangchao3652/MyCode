package com.lnu.fang.imooc_zhuanpan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Fang on 2015/11/15.
 */
public class LuckPanSV extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Canvas canvas;
    //用于绘制 线程
    private Thread t;
    /**
     * 线程的控制开关
     */
    private boolean isRunning;
    private String[] mstrs = new String[]{"单反相机", "iPad", "云南五日游", "Mp3", "清华", "恭喜发财"};

    private int[] mImages = new int[]{R.drawable.danfan,
            R.drawable.ipad, R.drawable.iphone,
            R.drawable.f015,
            R.drawable.meizi,
            R.drawable.f040};
    /**
     * 与图片对应的 bitmap
     */
    private Bitmap[] mBitmaps;
    /**
     * 背景图
     */
    private Bitmap mbgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
    /**
     * 文字大小
     */
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());


    /**
     * 盘快颜色
     */

    private int[] mColors = new int[]{0xFFFFc300, 0xFFF18e01, 0xFFFFc300, 0xFFF18e01, 0xFFFFc300, 0xFFF18e01};
    private int mItemCount = 6;

    /**
     * //绘制盘的画笔
     */
    private Paint mArcPaint;
    /**
     * 绘制文字的画笔
     */
    private Paint mTextPaint;//


    /**
     * 盘快的范围
     */

    private RectF mRectF = new RectF();

    /**
     * 整个盘块的直径
     */
    private int mRadius;


    /**
     * 盘快的滚动速度
     */
    private double mspeed = 0;

    private volatile int mStartAngel = 0;
    /**
     * 是否点击了停止按钮
     */
    private boolean isShouldEnd;

    /**
     * 转盘的中心位置（mCenter,mCenter）即中心点
     */
    private int mCenter;
    /**
     * 这里的padding取最小值
     */
    private int mpadding;

    public LuckPanSV(Context context) {
        this(context, null);
    }

    public LuckPanSV(Context context, AttributeSet attrs) {
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredHeight(), getMeasuredWidth());//整个surfaceview的宽和高
        mpadding = getPaddingLeft();
        //半径
        mRadius = width - mpadding * 2;

        //中心点
        mCenter = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //初始化画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);//抗锯齿
        mArcPaint.setDither(true);//是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰

        // 初始化画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(0xFFFFFFFF);//
        mTextPaint.setTextSize(mTextSize);
        //  初始化盘快绘制的范围
        mRectF = new RectF(mpadding, mpadding, mpadding + mRadius, mpadding + mRadius);

        //初始化图片
        mBitmaps = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mBitmaps[i] = BitmapFactory.decodeResource(getResources(), mImages[i]);
        }


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
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if (end - start < 50) {
                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                //绘制。。。
                //绘制背景
                drawBg();
                //绘制盘块
                float tempAngle = mStartAngel;
                float sweepAngle = 360 / mItemCount;
                for (int i = 0; i < mItemCount; i++) {
                    mArcPaint.setColor(mColors[i]);
                    /**
                     * oval :指定圆弧的外轮廓矩形区域()。
                     startAngle: 圆弧起始角度，单位为度。
                     sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度。
                     useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。
                     paint: 绘制圆弧的画板属性，如颜色，是否填充等。
                     */
                    canvas.drawArc(mRectF, tempAngle, sweepAngle, true, mArcPaint);

                    //绘制文本
                    drawText(tempAngle, sweepAngle, mstrs[i]);

                    drawIcon(tempAngle, mBitmaps[i]);
                    tempAngle += sweepAngle;
                }
                //家这句话开始旋转
                mStartAngel += mspeed;

                if (isShouldEnd) {
                    mspeed -= 1;//速速递减
                }

                if (mspeed <= 0) {
                    mspeed = 0;
                    isShouldEnd = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * 启动转盘
     */
    public void luckStart() {
        mspeed = 50;
        isShouldEnd = false;
    }

    /**
     * stop转盘
     */
    public void luckStop() {

        isShouldEnd = true;
    }

    /**
     * 转盘是否还在旋转
     * @return
     */
    public boolean isstart(){
        return mspeed!=0;
    }

    /**
     * 停止按钮是否按下
     * @return
     */
    public  boolean  isShouldEnd(){
        return  isShouldEnd;
    }
    /**
     * 绘制Icon
     *
     * @param mStartAngel
     * @param mBitmap
     */
    private void drawIcon(float mtempAngel, Bitmap mBitmap) {
        //首先得知道图片的宽度高度
        int imgWidth = mRadius / 8;//自己看UI比例设计
        //Math.PI/180
        float angle = (float) ((mtempAngel + 360 / mItemCount / 2) * Math.PI / 180);//角度转换为弧度 方便sin cos计算
        //图片中心点坐标 (x,y)
        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        //确定那个图片位置
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        /**
         * drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint)；
         Rect src: 是对图片进行裁截，若是空null则显示整个图片
         RectF dst：是图片在Canvas画布中显示的区域，
         大于src则把src的裁截区放大，
         小于src则把src的裁截区缩小。
         */
        canvas.drawBitmap(mBitmap, null, rect, mArcPaint);


    }


    private void drawText(float tempAngle, float sweepAngle, String mstr) {
        Path path = new Path();
        /**
         *http://blog.csdn.net/w124374860/article/details/44995057
         * addArc(RectF oval, float startAngle, float sweepAngle)方法：

         path.addArc方法用于绘制圆弧，这个圆弧取自RectF矩形的内接椭圆上的一部分，圆弧长度由后两个参数决定

         startAngle：起始位置的角度值

         sweepAngle：旋转的角度值
         */
        path.addArc(mRectF, tempAngle, sweepAngle);
        /**
         * //Offset参数指定水平偏移、vOffset指定垂直偏移 http://www.xuebuyuan.com/2071680.html
         * 水平偏移量=（区块弧度一半）---（文字长度的一半）
         */

        int textwideh = (int) mTextPaint.measureText(mstr);//Return the width of the text.
        int hoffset = (int) (mRadius * Math.PI / mItemCount / 2 - textwideh / 2);

        int vOffset = mRadius / 12;
        canvas.drawTextOnPath(mstr, path, hoffset, vOffset, mTextPaint);
    }

    /**
     * 绘制背景
     */
    private void drawBg() {
        canvas.drawColor(0xFFFFFFFF);//直接绘制背景
        canvas.drawBitmap(mbgBitmap, null, new RectF(mpadding / 2, mpadding / 2, mpadding * 3 / 2 + mRadius, mpadding * 3 / 2 + mRadius), mArcPaint);
    }
}
