package com.stylingandroid.snowfall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SnowView extends View {
    private static final int NUM_SNOWFLAKES = 50;
    private static final int DELAY = 5;

    private SnowFlake[] snowflakes;
    private Bitmap[] mBitmaps;

    private Bitmap bitmap;
    public SnowView(Context context) {
        super(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.yuanbao);
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void resize(int width, int height) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.WHITE);
//        paint.setStyle(Paint.Style.FILL);
        snowflakes = new SnowFlake[NUM_SNOWFLAKES];
        mBitmaps = new Bitmap[NUM_SNOWFLAKES];
        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
            mBitmaps[i] = changeBitmapSize(bitmap);
            snowflakes[i] = SnowFlake.create(width, height, paint,mBitmaps[i].getWidth(),mBitmaps[i].getHeight());
        }
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
