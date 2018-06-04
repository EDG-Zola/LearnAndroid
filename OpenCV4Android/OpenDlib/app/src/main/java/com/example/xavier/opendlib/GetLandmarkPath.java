package com.example.xavier.opendlib;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2018\6\2 0002.
 */

public class GetLandmarkPath {
    private static final String TAG = "GetLandmarkPath";
    private GetLandmarkPath(){}
    /**
     * getFaceShapeModelPath
     * @return default face shape model path
     */
    public static String getFaceShapeModelPath() {
        Log.d(TAG, "getFaceShapeModelPath: ");
        File sdcard = Environment.getExternalStorageDirectory();
        String targetPath = sdcard.getAbsolutePath() + File.separator + "shape_predictor_68_face_landmarks.dat";
        if (targetPath != null)
            return targetPath;
        else
            return "false";
    }
}
