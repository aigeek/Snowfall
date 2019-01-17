package com.stylingandroid.snowfall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.TreeMap;

public class SnowView extends View {
    private static final int NUM_SNOWFLAKES = 200;
    private static final int DELAY = 5;

    private SnowFlake[] snowflakes;
    private Bitmap[] mBitmaps;
    private SnowFlake mSnowflake;
    private Bitmap mBitmap;

    private Bitmap bitmap;

    private TreeMap<Integer,Integer> yuanbaos;

    public SnowView(Context context) {
        super(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.yuanbao);
        yuanbaos = new TreeMap<>();
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void resize(int width, int height) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        snowflakes = new SnowFlake[NUM_SNOWFLAKES];
        mBitmaps = new Bitmap[NUM_SNOWFLAKES];
        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
            mBitmaps[i] = changeBitmapSize(bitmap);
            snowflakes[i] = SnowFlake.create(yuanbaos,width, height, paint,mBitmaps[i].getWidth(),mBitmaps[i].getHeight());
        }
//        mBitmap = bitmap;
//        mSnowflake = SnowFlake.create(yuanbaos,width, height, paint,mBitmap.getWidth(),mBitmap.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            resize(w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < snowflakes.length; i++) {
            snowflakes[i].draw(canvas,mBitmaps[i]);
        }
        getHandler().postDelayed(runnable, DELAY);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    private Bitmap changeBitmapSize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //计算压缩的比率
        float scale = (float) (Math.random() / 2 + 0.5f);

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        //获取新的bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.getWidth();
        bitmap.getHeight();
        return bitmap;
    }
}
