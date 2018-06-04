#include <jni.h>
#include "stdio.h"
#include "opencv2/opencv.hpp"

#include <glog/logging.h>


#include <dlib/image_processing/frontal_face_detector.h>
#include <dlib/opencv.h>
#include <dlib/image_processing/render_face_detections.h>
#include <dlib/image_processing.h>
#include <dlib/gui_widgets.h>

/* Header for class com_example_xavier_opendlib_OpencvClass */
using namespace cv;
using namespace std;
using namespace dlib;

#ifndef _Included_com_example_xavier_opendlib_OpencvClass
#define _Included_com_example_xavier_opendlib_OpencvClass
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_xavier_opendlib_OpencvClass
 * Method:    facedetection
 * Signature: (J)V
 */
void cannyDetection(Mat& img, Mat& dst);
void faceDetectionDlib(Mat& img, Mat& dst);
void renderToMat(std::vector<full_object_detection>& dets, Mat& dst);
JNIEXPORT void JNICALL Java_com_example_xavier_opendlib_OpencvClass_facedetection
  (JNIEnv *, jclass, jlong addrRgba, jlong addrOutput);

#ifdef __cplusplus
}
#endif
#endif
