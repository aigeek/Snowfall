package com.stylingandroid.snowfall;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

//https://blog.csdn.net/bboyfeiyu/article/details/50926928
//https://github.com/aigeek/Snowfall
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        SnowView snowView = new SnowView(this);
        relativeLayout.addView(snowView,layoutParams);
        setContentView(relativeLayout);
    }
}
