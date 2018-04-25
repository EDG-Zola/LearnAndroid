package com.example.cvlab.faciallandmarkdetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * Created by Administrator on 2018\4\24 0024.
 */

public class DetailActivity extends AppCompatActivity{
    ImageView imageView;
    Button btnProcess;
    Bitmap bmpInput, bmpOutput;
    Mat matInput, matOutput;

    static {
        System.loadLibrary("Mylibs");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //获取image view
        imageView = (ImageView) findViewById(R.id.imageView);
        btnProcess = (Button) findViewById(R.id.btnProcess);

        //获取图片的路径
        String photoPath = Environment.getExternalStorageState() + "/frames/frame.png";

        //获取位图图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmpInput = BitmapFactory.decodeFile(photoPath, options);
        //imageView加载为位图图片
        imageView.setImageBitmap(bmpInput);
        matOutput = new Mat(matInput.rows(), matInput.cols(), CvType.CV_8UC4);

        //按键操作进行图片处理
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "调用Native函数", Toast.LENGTH_SHORT).show();
                NativeClass.LandmarkDetection(matInput.getNativeObjAddr(), matOutput.getNativeObjAddr());
                bmpOutput = convertMat2Bitmap(matOutput);
                imageView.setImageBitmap(bmpOutput);
            }
        });
    }

    Bitmap convertMat2Bitmap(Mat img){
        int width = img.width();
        int height = img.height();

        Bitmap bmp;
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Mat temp;
        temp = img.channels() == 1? new Mat(width, height, CvType.CV_8UC1, new Scalar(1)) : new Mat(width, height, CvType.CV_8UC3, new Scalar(1));
        try{
            if (img.channels() == 3)
                cvtColor(img, temp, Imgproc.COLOR_RGB2BGRA);
            else if(img.channels() == 1)
                cvtColor(img, temp, Imgproc.COLOR_GRAY2BGRA);
            Utils.matToBitmap(temp, bmp);
        }catch (CvException e){
            Log.d("Excpeption", e.getMessage());
        }
        return bmp;
    }
    Mat convertBitmap2Mat(Bitmap rgbImage){
        //把java的Bitmap转换为OpenCV的Mat
        Mat rgbaMat = new Mat(rgbImage.getHeight(), rgbImage.getWidth(), CvType.CV_8UC4);
        Bitmap bmp32 = rgbImage.copy(Bitmap.Config.ARGB_8888, true);

        Mat rgbMat = new Mat(rgbImage.getHeight(), rgbImage.getWidth(), CvType.CV_8UC3);
        cvtColor(rgbaMat, rgbMat, Imgproc.COLOR_RGBA2BGR, 3);
        return rgbMat;
    }
}
