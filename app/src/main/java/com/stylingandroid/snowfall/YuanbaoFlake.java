package com.stylingandroid.snowfall;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.util.TreeMap;

import static com.stylingandroid.snowfall.SnowView.NUM_SNOWFLAKES;

/**
 * Created by yao on 2019 2019/1/18 at 14:32
 * 通过新建drawable的方式新建
 */
public class YuanbaoFlake {

    private int index;
    private Paint mPaint;
    private Drawable mDrawable;
    private Context mContext;
    private int yuanbaoWidth;
    private int yuanbaoHeight;
    private int width;
    private int height;
    private float scale;
    private TreeMap<Integer, Integer> yuanbaos;
    private static int orginWidth = 234;
    private static int orginHeight = 192;
    private float angle;
    private final Point position;
    private boolean isDown = false;

    private final float increment;

    public YuanbaoFlake(Context context, int index, Drawable mDrawable, Point position, float angle,
                        Paint paint, int yuanbaoWidth, int yuanbaoHeight, Float scale, TreeMap<Integer, Integer> yuanbaos,
                         int width, int height,float increment) {
        this.mContext = context;
        this.index = index;

        this.mDrawable = mDrawable;
        this.position = position;
        this.angle = angle;
        this.mPaint = paint;


        this.yuanbaoWidth = yuanbaoWidth;
        this.yuanbaoHeight = yuanbaoHeight;

        this.scale = scale;


        this.yuanbaos = yuanbaos;

        this.width = width;
        this.height = height;

        this.increment = increment;


    }

    public static YuanbaoFlake create(Context context, int index, Paint paint, int yuanbaoWidth, int yuanbaoHeight,
                                      Float scale, TreeMap<Integer, Integer> yuanbaos,int width, int height) {

        Random random = new Random();
        //即使多次创建，内存中也只会存在一份bitmap
        Drawable mDrawable = ContextCompat.getDrawable(context,R.drawable.yuanbao);

        yuanbaoHeight = (int) (scale * orginHeight);
        yuanbaoWidth = (int) (scale * orginWidth);



        int x = random.getRandom(width);
        int y = 0;
        if (index < NUM_SNOWFLAKES/20){//前10个
            y = -yuanbaoHeight;
        }else if (index < NUM_SNOWFLAKES/4){//随机20-50个
            y = -5*yuanbaoHeight-random.getRandom(height/2);
        }else if (index < NUM_SNOWFLAKES){//中间50-200个
            y = -7*yuanbaoHeight;
        }
        Point position = new Point(x, y);
        yuanbaos.put(x,y);
        float increment = random.getRandom(2f, 4f);
        //起始角度
        float angle = (float) (Math.PI/8);
        mDrawable.setBounds(x,y,x+yuanbaoWidth,y+yuanbaoHeight);


        return new YuanbaoFlake(context,index,mDrawable,position,angle,paint, yuanbaoWidth, yuanbaoHeight,scale, yuanbaos,
                width,height,increment);
    }

    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        move(width, height);
        if (position.y < -10){
            return;
        }else {
            mDrawable.draw(canvas);
        }
    }


    private void move(int width, int height) {
        int x = position.x;
        int y = position.y;
        if (isDown){
            return;
        }
        //已经出现的的使用重力加速度
        if (position.y >= -5*yuanbaoHeight){//起始加速点.决定了显示在屏幕上的加速度
            if (isDown(width,height)) {
                //附近的元宝增加flakeHeight/3的高度
                if (index < NUM_SNOWFLAKES/2){

//                if ((yuanbaos.higherKey(x) != null && (yuanbaos.higherKey(x) - x < flakeWidth / 2))
//                        || (yuanbaos.lowerKey(x) != null) && (x - yuanbaos.lowerKey(x) < flakeWidth / 2)) {
                    y = position.y - yuanbaoHeight / 3;
                } else if (yuanbaos.containsKey(x)) {//相同的x位置元宝增加flakeHeight/2的高度
                    y = position.y - yuanbaoHeight / 2;
                }else {//其余元宝
                    y = position.y - yuanbaoHeight;
                }
                isDown = true;
            }else {
                isDown = false;
                y = getAccelerateValue(position.y);
            }
        }else {//没有出现的递增使其出现
            y +=increment;
        }

        position.set((int) x, (int) y);

    }

    private boolean isDown(int width, int height) {
        if (height-position.y < yuanbaoHeight*0.75){
            return true;
        }
        return false;
    }

    private int getAccelerateValue(int y){
        //通过余弦函数模拟加速度
        angle += 25f/10000f *Math.PI;// pi/4 * 1/100;
        //y叠加
        y = (int) (y+60*increment*(1-Math.cos(angle)));
        return y;
    }

    static class Random {
        private static final java.util.Random RANDOM = new java.util.Random();

        public float getRandom(float lower, float upper) {
            float min = Math.min(lower, upper);
            float max = Math.max(lower, upper);
            return getRandom(max - min) + min;
        }

        public float getRandom(float upper) {
            return RANDOM.nextFloat() * upper;
        }

        public int getRandom(int upper) {
            return RANDOM.nextInt(upper);
        }

    }
}
