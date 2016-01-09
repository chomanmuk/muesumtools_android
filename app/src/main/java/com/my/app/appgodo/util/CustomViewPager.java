package com.my.app.appgodo.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Mark on 2015-12-21.
 */
public class CustomViewPager extends ViewPager {
    private boolean mIsEnabled;
    private float pointX1 = 0;
    private float pointX2 = 0;
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsEnabled = false;
    }

    public CustomViewPager(Context context) {
        super(context);
        mIsEnabled = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mIsEnabled) {
            return super.onInterceptTouchEvent(event);
        } else {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN :
                    pointX1 = event.getX();
                    break;
                case MotionEvent.ACTION_UP :
                    pointX2 = event.getX();
                    break;
            }
            System.out.println(pointX1 + " - " + pointX2);
            return false;
        }
    }
}
