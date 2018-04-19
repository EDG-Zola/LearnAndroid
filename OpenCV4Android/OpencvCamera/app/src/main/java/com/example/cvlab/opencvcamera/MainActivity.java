package com.example.cvlab.opencvcamera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    private static final String TAG = "MainActivity";
    JavaCameraView javaCameraView;//创建一个javaCameraView的对象
    Mat mRgba, imgGray, imgCanny;//创建三个个Mat对象，用来显示彩色图，灰度图，和Canny化图片
    //创建一个回调函数,当回调成功时，显示javaCameraView，否则调用基类的onManagerConnected()方法
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
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
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);//将布局中id为java_camera_view的控件绑定到对象javaCameraView
        javaCameraView.setVisibility(SurfaceView.VISIBLE);//设置为可见状态
        javaCameraView.setCvCameraViewListener(this);//设置一个监听器
    }

    //重载活动周期状态的一些函数

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //OpenCV加载成功，调用回调函数
        if (OpenCVLoader.initDebug()){
            Log.i(TAG, "Opencv loaded successfullt.");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        //否则，重新加载OpenCV
        else{
            Log.i(TAG, "Opencv not loaded.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallBack);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);//4通道的Mat对象，r,g,b,alpha
        imgGray = new Mat(height, width, CvType.CV_8UC1);
        imgCanny = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //获取摄像头的每一帧并Canny化
        mRgba = inputFrame.rgba();//获取输入每一帧的彩色图像
        Imgproc.cvtColor(mRgba, imgGray, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.Canny(imgGray, imgCanny, 50, 150);
        return imgCanny;
    }
}
