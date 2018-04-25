package com.example.cvlab.faciallandmarkdetection;

/**
 * Created by Administrator on 2018\4\24 0024.
 */

public class NativeClass {
    public native static void getMessage();
    public native static void LandmarkDetection(long addrInput, long addrOutput);
}
