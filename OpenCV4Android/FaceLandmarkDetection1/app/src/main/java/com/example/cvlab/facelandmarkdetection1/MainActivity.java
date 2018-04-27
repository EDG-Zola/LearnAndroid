package com.example.cvlab.facelandmarkdetection1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "MainActivity";

    JavaCameraView javaCameraView;
    Mat mRgba;
    BaseLoaderCallback mLoderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }

        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("MyLibs");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

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

    @Override
    protected void onResume() {
        super.onPostResume();
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded successfully.");
            mLoderCallback.onManagerConnected(mLoderCallback.SUCCESS);
        } else {
            Log.i(TAG, "OpenCV not loaded.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoderCallback);
        }
    }

    private void saveImage(Mat subImg) {
        //创建一个位图格式（.bmp）的文件,将摄像头获取的图片保存为位图格式
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(subImg.cols(), subImg.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(subImg, bmp);
        } catch (CvException e) {
            Log.d(TAG, e.getMessage());
        }
        subImg.release();

        FileOutputStream out = null;
        String filename = "frame.png";

        File sd = new File(Environment.getExternalStorageDirectory() + "/frames");
        boolean success = true;
        //如果外部存储路径不存在，则新建一个
        if (!sd.exists()) {
            sd.mkdir();
        }
        if (success) {
            //创建包含存储文件的绝对路径
            File dest = new File(sd, filename);
            try {
                out = new FileOutputStream(dest);
                //将.bmp格式压缩为png格式,参数100表示为最大质量压缩，即无损压缩
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            } finally {
                try {
                    //存储成功
                    if (out != null) {
                        out.close();
                        Log.d(TAG, "OK!");
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage() + "Error");
                    e.getStackTrace();
                }
            }
        }
    }

    //按键操作
    public void onClickGo(View view) {
        //保存摄像头拍摄的图片
        saveImage(mRgba);
        //创建显示Intent在MainActivity和DetailActivity间穿梭
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        return mRgba;
    }
}
