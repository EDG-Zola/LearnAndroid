#include <com_example_cvlab_opencvhumandetection1_OpencvClass.h>

//检测行人
JNIEXPORT void JNICALL Java_com_example_cvlab_opencvhumandetection1_OpencvClass_humandetection
(JNIEnv *, jclass, jlong addrRgba){
    Mat& frame = *(Mat*) addrRgba;
    detecthuman(frame);
}

void detecthuman(Mat &frame){
    //调用手机存储的haarcascade_fullbody.xml文件
    String human_cascade_name = "/storage/emulated/0/data/haarcascade_fullbody.xml";
    CascadeClassifier human_cascade;
    if( !human_cascade.load( human_cascade_name ) ){ printf("--(!)Error loading\n"); return; };

    std::vector<Rect> humans;
    Mat frame_gray;
    cvtColor( frame, frame_gray, CV_BGR2GRAY );
    equalizeHist( frame_gray, frame_gray );
    human_cascade.detectMultiScale( frame_gray, humans, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30) );

    for( size_t i = 0; i < humans.size(); i++ ) {
        //绘制行人方框
        rectangle(frame, Point(humans[i].x, humans[i].y), Point(humans[i].x + humans[i].width, humans[i].y + humans[i].height),
                  Scalar(0, 255, 0));
    }
}
