#include <com_example_cvlab_ndkopencvtest1_OpencvNativeClass.h>

JNIEXPORT jint JNICALL Java_com_example_cvlab_ndkopencvtest1_OpencvNativeClass_convertGray
  (JNIEnv *, jclass, jlong addrRgba, jlong addrGray){
    Mat& mRgba = *(Mat*) addrRgba;
    Mat& mGray = *(Mat*) addrGray;

    int conv;
    jint retVal;
    conv = toGray(mRgba, mGray);

    retVal = (jint) conv;
    return retVal;
}

int toGray(Mat img, Mat gray){
    cvtColor(img, gray, COLOR_BGR2GRAY);
    if (gray.rows == img.rows && gray.cols == img.cols)
        return 1;
    return 0;
}
