package com.example.wanghui.jumpanimation.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.wanghui.jumpanimation.utils.DataHelper;
import com.example.wanghui.jumpanimation.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private ImageView mIvJumper;
    private RelativeLayout mRlJumpLayout;
    private Button mButton;
    private HashMap<Integer, int[]> mLocations = new HashMap();
    private HashMap<Integer, View> mChildren = new HashMap();
    private float[][] points = new float[3][2];
    private DataHelper dataHelper;
    private int mark;
    private float[] offset;

    private float a;
    private float b;
    private float c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initLocationsAndChildrenView() {
        int count = mRlJumpLayout.getChildCount();
        for (int i = 0; i < count; i ++){
            View view = mRlJumpLayout.getChildAt(i);
            int[] location = new int[2];
            view.getLocationInWindow(location);
            mLocations.put(i, location);
            mChildren.put(i, view);
        }
    }

    private void initABC(int childPosition1, int childPosition2) {
        View viewFirst = mChildren.get(childPosition1);
        int[] locationFirst = mLocations.get(childPosition1);
        View viewSecond = mChildren.get(childPosition2);
        int[] locationSecond = mLocations.get(childPosition2);
        points[0][0] = locationFirst[0] + viewFirst.getMeasuredWidth()/2f;
        points[0][1] = locationFirst[1];
        points[1][0] = locationSecond[0] + viewSecond.getMeasuredWidth()/2f;
        points[1][1] = locationSecond[1];
        points[2][0] = (points[0][0] + points[1][0])/2f;
        points[2][1] = points[0][1]- 100;

        if(childPosition1 == 0){
            initDataHelper();
        }else {
            dataHelper.refreshPoints(points);
        }

        float[] floats = dataHelper.calculateParArguments();
        a = floats[0];
        b = floats[1];
        c = floats[2];
        Log.d(TAG, a + "----" + b + "----" + c);
    }

    private void initDataHelper(){
        dataHelper = new DataHelper(points);
        offset = new float[]{points[0][0], points[0][1]};
        dataHelper.setOffset(offset);
    }

    private void initView() {
        mRlJumpLayout = (RelativeLayout) findViewById(R.id.rl_jump_animation);
        mIvJumper = (ImageView) findViewById(R.id.iv_jumper);
        mButton = (Button) findViewById(R.id.btn_begin);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClick();
            }
        });
    }

    private void onStartClick() {
        mark = 0;
        initLocationsAndChildrenView();
        initABC(mark, mark + 1);
        startAnimation();
    }

    private void startAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mIvJumper, "translationX", points[0][0] - offset[0], points[1][0]-offset[0]);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "animationX----------------" + (float) animation.getAnimatedValue());
                Log.d(TAG, "animationY----------------" + getY((Float) animation.getAnimatedValue()));
                mIvJumper.setTranslationY(getY((Float) animation.getAnimatedValue()));
            }
        });
        objectAnimator.setupStartValues();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mark ++;
                if(mark < mLocations.size() - 1){
                    initABC(mark, mark + 1);
                    startAnimation();
                }else if(mark == mLocations.size() - 1){

                }else {
                    return;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private float getY(float x){
        return a*x*x + b*x + c;
    }
}
