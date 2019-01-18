package com.stylingandroid.snowfall;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.TreeMap;

import static com.stylingandroid.snowfall.SnowView.NUM_SNOWFLAKES;

class SnowFlake {
    private static final float ANGE_RANGE = 0.1f;
    private static final float HALF_ANGLE_RANGE = ANGE_RANGE / 2f;
    private static final float HALF_PI = (float) Math.PI / 2f;
    private static final float ANGLE_SEED = 25f;
    private static final float ANGLE_DIVISOR = 10000f;
    private static final float INCREMENT_LOWER = 2f;
    private static final float INCREMENT_UPPER = 4f;
    private static final float FLAKE_SIZE_LOWER = 7f;
    private static final float FLAKE_SIZE_UPPER = 20f;

    private final Random random;
    private final Point position;
    private float angle;
    private double angle0;
    private final float increment;
    private final float flakeSize;
    private final TreeMap<Integer,Integer> yuanbaos;
    private final Paint paint;
    int flakeWidth;
    int flakeHeight;
    private boolean isDown = false;
    private int index;

    public static SnowFlake create(int index, TreeMap<Integer, Integer> yuanbaos, int width, int height, Paint paint, int flakeWidth, int flakeHeight) {
        Random random = new Random();
        int x = random.getRandom(width);
        int y = 0;
        if (index < NUM_SNOWFLAKES/20){//前10个
            y = -flakeHeight;
        }else if (index < NUM_SNOWFLAKES/4){//随机20-50个
            y = -5*flakeHeight-random.getRandom(height/2);
        }else if (index < NUM_SNOWFLAKES){//中间50-200个
            y = -7*flakeHeight;
        }
        Point position = new Point(x, y);
        yuanbaos.put(x,y);
        //起始角度
        float angle = (float) (Math.PI/8);
        float increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
        float flakeSize = random.getRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER);
        return new SnowFlake(index,yuanbaos,random, position, angle, increment, paint,flakeWidth,flakeHeight,flakeSize);
    }

    SnowFlake(int index,TreeMap<Integer, Integer> yuanbaos, Random random, Point position, float angle, float increment, Paint paint, int flakeWidth, int flakeHeight, float flakeSize) {
        this.random = random;
        this.position = position;
        this.angle0 = angle;
        this.increment = increment;
        this.paint = paint;
        this.flakeWidth = flakeWidth;
        this.flakeHeight = flakeHeight;
        this.flakeSize = flakeSize;
        this.yuanbaos = yuanbaos;
        this.index = index;
    }

    private void move(int width, int height) {
        int x = position.x;
        int y = position.y;
        if (isDown){
            return;
        }
        //已经出现的的使用重力加速度
        if (position.y >= -5*flakeHeight){//起始加速点.决定了显示在屏幕上的加速度
            if (isDown(width,height)) {
                //附近的元宝增加flakeHeight/3的高度
                if (index < NUM_SNOWFLAKES/2){

//                if ((yuanbaos.higherKey(x) != null && (yuanbaos.higherKey(x) - x < flakeWidth / 2))
//                        || (yuanbaos.lowerKey(x) != null) && (x - yuanbaos.lowerKey(x) < flakeWidth / 2)) {
                    y = position.y - flakeHeight / 3;
                } else if (yuanbaos.containsKey(x)) {//相同的x位置元宝增加flakeHeight/2的高度
                    y = position.y - flakeHeight / 2;
                }else {//其余元宝
                    y = position.y - flakeHeight;
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

    private void stackBitmap() {
//        position.y;
    }

    private boolean isDown(int width, int height) {
        if (height-position.y < flakeHeight*0.75){
            return true;
        }
        return false;
    }

    private int getAccelerateValue(int y){
        //通过余弦函数模拟加速度
        angle0 += ANGLE_SEED/ANGLE_DIVISOR *Math.PI;// pi/4 * 1/100;
        //y叠加
        y = (int) (y+60*increment*(1-Math.cos(angle0)));
        return y;
    }

    //循环从上面出现
    private boolean isInside(int width, int height) {
        int x = position.x;
        int y = position.y;
        return x >= -flakeSize - 1 && x + flakeSize <= width && y >= -flakeSize - 1 && y - flakeSize < height;
    }

    private boolean isFlowOutside(int width, int height) {
        int x = position.x;
        int y = position.y;
        return  y > height;
    }

    private void reset(int width) {
        position.x = random.getRandom(width);
        position.y = (int) (-flakeSize - 1);
        angle = random.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
    }

    private void resetTest(int width) {
        position.x = width/2;
        position.y = -flakeHeight;
        angle0 = ANGLE_SEED/ANGLE_DIVISOR *Math.PI;// pi/4 * 1/100;
    }

    public void draw(Canvas canvas,Bitmap bitmap) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        move(width, height);
        if (bitmap != null){
            if (position.y < -10){
                return;
            }
            canvas.save();
            canvas.translate(position.x,position.y);
            canvas.drawBitmap(bitmap, 0,0, paint);
            canvas.restore();
        }
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
