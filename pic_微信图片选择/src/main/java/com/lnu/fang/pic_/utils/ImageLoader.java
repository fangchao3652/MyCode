package com.lnu.fang.pic_.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Fang on 2015/11/18.
 */
public class ImageLoader {
    private static ImageLoader mInstance;
    private LruCache<String, Bitmap> mLruCache;
    private ExecutorService mThreadPool;//线程池 可用于让哪个runnable先执行  FIFO  LIFO
    private static final int DEFAULT_THREAD_COUNT = 1;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;

    public enum Type {
        FIFO, LIFO
    }

    /**
     * 任务列表
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;
    /**
     * 用于确保先生成PoolThreadHandler对象
     */
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);//默认是0个
    private Semaphore mSemaphorePoolThread;//先不new

    private ImageLoader(int threadCount, Type type) {
        init(threadCount, type);
    }

    /**
     * 初始化操作
     *
     * @param threadCount
     * @param type
     */
    private void init(int threadCount, Type type) {
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //取出一个任务进行执行
                        mThreadPool.execute(getTask());
                        try {
                            //假如最大允许有三个，那么第四个就会阻塞在这里  当loadImage加载压缩完毕后 在那里释放一个信号量，第四个会执行，但这时的第四个就不是第四个加进去的那个了，这就要有gettask里的LIFO和FIFO决定了
                            mSemaphorePoolThread.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                };
                //释放信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //getByteCount() = getRowBytes() * getHeight()
                return value.getByteCount();
            }
        };


        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = Type.LIFO;


        mSemaphorePoolThread = new Semaphore(threadCount);
    }

    /**
     * 这里用到了mPoolThreadHandler 而他有可能还没有被创建出来，所以为了处理并发我们用信号量 控制一下  在该方法里请求一个信号量，若没有则一直阻塞相当于wait
     * mPoolThreadHandler创建完毕后释放信号量 相当于notify
     *
     * @param runnable
     */
    private synchronized void addTasks(Runnable runnable) {
        //加到队列中
        mTaskQueue.add(runnable);

        //发送通知
        //if(mPoolThreadHandler==null) wait
        try {
            if (mPoolThreadHandler == null)
                mSemaphorePoolThreadHandler.acquire();//默认是0个 现在去请求会阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x111);

    }

    /**
     * 从任务队列取出一个方法
     *
     * @return
     */
    private Runnable getTask() {
        if (mType == Type.FIFO) {
            return mTaskQueue.removeFirst();//顺序加载  这样等待时间较长
        } else if (mType == Type.LIFO) {
            return mTaskQueue.removeLast(); //先加载当前的 先执行最新的task
        } else
            return null;
    }

    /**
     * 根据path 为imageview设置图片
     *
     * @param path
     * @param imageview111
     */
    public void loadImage(final String path, final ImageView imageview111) {
        imageview111.setTag(path);
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //获取得到的图片  为imageview回调设置图片
                    ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.path;
                    //将path与imageview存贮的tag进行对比  imageview可能进行复用了,这样imageview虽然还是那个对象但可能已经在第二屏了，这个加载完过来的bm是第一屏时的那个imageview请求的，容易混乱，用tag进行区分能避免这种情况，因为第二屏的tag变了
                    if (imageView.getTag().toString().equals(path)) {
                        imageView.setImageBitmap(bm);
                    }
                }
            };
        }

        Bitmap bm = getBitmapFromLruCache(path);
        if (bm != null) {
            reFreshBitmap(path, imageview111, bm);//***********向主线程 发送这个图片
        } else {
            addTasks(new Runnable() {
                @Override
                public void run() {
//加载图片
                    //图片压缩 option
                    //1.获得图片需要显示的大小
                    ImageSize imageSize = getImageviewSize(imageview111);
                    //2压缩图片
                    Bitmap bm = decodeSampleBitmapFromPath(imageSize.width, imageSize.height, path);
                    //3 加入到缓存
                    addBitmapToLruCache(path, bm);
                    //4
                    reFreshBitmap(path, imageview111, bm);//***********向主线程 发送这个图片
                    mSemaphorePoolThread.release();
                }
            });
        }
    }

    private void reFreshBitmap(String path, ImageView imageview111, Bitmap bm) {
        Message msg = Message.obtain();
        ImageBeanHolder holder = new ImageBeanHolder();
        holder.bitmap = bm;
        holder.imageView = imageview111;
        holder.path = path;
        msg.obj = holder;

        mUIHandler.sendMessage(msg);
    }

    /**
     * 加入缓存
     *
     * @param path
     * @param bm
     */
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null) {
            if (bm != null) {
                mLruCache.put(path, bm);
            }
        }
    }

    /**
     * 根据图片需要显示的宽和高对图片进行压缩
     *
     * @param width
     * @param height
     * @param path
     * @return
     */
    private Bitmap decodeSampleBitmapFromPath(int width, int height, String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//获取宽和高并不加载到内存
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInsampleSizze(options, width, height);
        options.inJustDecodeBounds = false;
        //再次解析图片
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据目标图片大小来计算Sample图片大小
     *
     * @param options
     * @return
     */
    private int caculateInsampleSizze(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 根据imageview获取适当的压缩的宽和高
     *
     * @param imageview
     * @return
     */
    private ImageSize getImageviewSize(ImageView imageview) {
        DisplayMetrics displayMetrics = imageview.getContext().getResources().getDisplayMetrics();


        ImageSize imageSize = new ImageSize();
        ViewGroup.LayoutParams lp = imageview.getLayoutParams();
        int width = lp.width == ViewGroup.LayoutParams.WRAP_CONTENT ? 0 : imageview
                .getWidth(); // Get actual image width
        if (width <= 0)
            width = lp.width; // Get layout width parameter
        if (width <= 0)
            width = getImageviewFieldValue(imageview, "mMaxWidth"); // Check
        // maxWidth
        // parameter
        if (width <= 0)
            width = displayMetrics.widthPixels;
        int height = lp.height == GridLayout.LayoutParams.WRAP_CONTENT ? 0 : imageview
                .getHeight(); // Get actual image height
        if (height <= 0)
            height = lp.height; // Get layout height parameter
        if (height <= 0)
            height = getImageviewFieldValue(imageview, "mMaxHeight"); // Check
        // maxHeight
        // parameter
        if (height <= 0)
            height = displayMetrics.heightPixels;
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    class ImageSize {
        int width;
        int height;
    }


    /**
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key) {
        return mLruCache.get(key);
    }

    /**
     * 避免 loadImage时造成混乱
     */
    private class ImageBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    /**
     * 单例获得该实例对象
     *
     * @return
     */
    public static ImageLoader getInstance(int threadCount, Type type)
    {

        if (mInstance == null)
        {
            synchronized (ImageLoader.class)
            {
                if (mInstance == null)
                {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(DEFAULT_THREAD_COUNT, Type.LIFO);
                }
            }
        }
        return mInstance;
    }


    /**
     * 为了兼容API<16的
     * 通过反射获取IV 的某个属性值
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageviewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageLoader.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            int filedValue = field.getInt(object);
            if (filedValue > 0 && filedValue < Integer.MAX_VALUE) {
                value = filedValue;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return value;
    }
}
