package com.example.cvlab.ndkopencvtest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
//4.要使用OpenCV的相机接口CameraBridgeViewBase.CvCameraViewListener2,并继承三个基类的函数
//5.创建Mat类的对象,并初始化、释放内存、获取帧图像
public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    //1.首先创建一个JavaCameraView类的对象javaCameraView来使用OpenCV相机的库函数
    //并通过一个回调函数对象mLoderCallBack来判断javaCameraView的状态，从而控制javaCameraView
    private static final String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    Mat mRgba, mGray;
    BaseLoaderCallback mLoderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    javaCameraView.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };
    // Used to load the 'MyOpencvLibs' library on application startup.
    static {
        System.loadLibrary("MyOpencvLibs");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //2.绑定javaCameraView到布局控件上，并设置可见状态
        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        // Example of a call to a native method

    }
    //3.创建MainActicity三个生命周期的函数来控制javaCameraView的显示状态
    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }
    //在onResume状态调用OpenCV
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (OpenCVLoader.initDebug()){
            Log.i(TAG, "OpenCV loaded successfully.");
            mLoderCallBack.onManagerConnected(mLoderCallBack.SUCCESS);
        }
        else {
            Log.i(TAG, "OpenCV not loaded.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoderCallBack);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    //5.1初始化mRgba
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
    }

    //5.2释放mRgba的内存
    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
    }

    //5.3获取每一帧，并显示
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        OpencvNativeClass.convertGray(mRgba.getNativeObjAddr(), mGray.getNativeObjAddr());
        return mGray;
    }
}
