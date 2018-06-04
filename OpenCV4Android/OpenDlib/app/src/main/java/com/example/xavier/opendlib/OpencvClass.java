package com.example.xavier.opendlib;

import android.util.Log;

/**
 * Created by Administrator on 2018\5\31 0031.
 */

public class OpencvClass {
    public native static void facedetection(long addrRgba, long addrOutput);
}

