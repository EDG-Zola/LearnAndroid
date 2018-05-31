#include <jni.h>
#include "stdio.h"
#include "opencv2/opencv.hpp"
/* Header for class com_example_xavier_opendlib_OpencvClass */
using namespace cv;
using namespace std;
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
void detect(Mat &frame);
JNIEXPORT void JNICALL Java_com_example_xavier_opendlib_OpencvClass_facedetection
  (JNIEnv *, jclass, jlong addrRgba);

#ifdef __cplusplus
}
#endif
#endif
