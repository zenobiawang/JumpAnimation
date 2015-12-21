package com.example.wanghui.jumpanimation.utils;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by wanghui on 2015/12/16.
 */
public class DataHelper {
    private final String TAG = "DataHelper";
    private float[][] points;
    private float[] offset;
    public DataHelper(float[][] points) {
        this.points = points;
    }


    public void setOffset(float[] offset) {
        this.offset = offset;
    }

    public void refreshPoints(float[][] points){
        this.points = points;
    }

    /**
     * 通过抛物线上三点坐标得到抛物线方程：y = ax^2 + b*x +c中的a、b、c
     * 其中a：arguments[0]、b：arguments[1]、c：arguments[2]
     * @return
     */
    public float[] calculateParArguments(){
        if(points.length < 3){
            Log.d(TAG, "points is invalidity!");
            return null;
        }

        float[] arguments = new float[3];
        float x1 = points[0][0] - offset[0];
        float y1 = points[0][1] - offset[1];
        float x2 = points[1][0] - offset[0];
        float y2 = points[1][1] - offset[1];
        float x3 = points[2][0] - offset[0];
        float y3 = points[2][1] - offset[1];
        arguments[0] = (y1 * (x2 - x3) + y2 * (x3 - x1) + y3 * (x1 - x2))
                / (x1 * x1 * (x2 - x3) + x2 * x2 * (x3 - x1) + x3 * x3 * (x1 - x2));
        arguments[1] = (y1 - y2) / (x1 - x2) - arguments[0] * (x1 + x2);
        arguments[2] = y1 - (x1 * x1) * arguments[0] - x1 * arguments[1];
        return arguments;
    }
}
