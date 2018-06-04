#include "com_example_xavier_opendlib_OpencvClass.h"
JNIEXPORT void JNICALL Java_com_example_xavier_opendlib_OpencvClass_facedetection
        (JNIEnv *, jclass, jlong addrRgba, jlong addrOutput){
    Mat& image = *(Mat*) addrRgba;
    Mat& dst = *(Mat*) addrOutput;
    //cannyDetection(image, dst);
    faceDetectionDlib(image, dst);
}

//dlib的图像格式是array2d，opencv的图像格式是mat
inline void faceDetectionDlib(Mat& img, Mat& dst){
    LOG(INFO) << "faceDetectionDlib";
    try {

        /*****************由于每张照片都会读取.dat数据，因此速度回非常慢******************/
        /*****************因此需要查Android中如何先读取dat数据，再做图像处理******************/
        //人脸检测器
        frontal_face_detector detector = get_frontal_face_detector();
        //导入预测模型数据
        shape_predictor sp;
        deserialize("storage/emulated/0/shape_predictor_68_face_landmarks.dat") >> sp;
        //cimg是opencv图像格式
        dlib::cv_image<dlib::bgr_pixel> cimg(img);

        // 现在告诉面部检测器，获取图像中所有面部的边界框列表
        std::vector<dlib::rectangle> dets = detector(cimg);
        // 现在我们将用shape_predictor告诉我们检测到的每张脸的姿势，
        std::vector<full_object_detection> shapes;
        for (unsigned long j = 0; j < dets.size(); ++j) {
            full_object_detection shape = sp(cimg, dets[j]);
            // 这里的shape_predictor_68_face_landmarks.dat只支持68个特征点，
            // 这些特征点的像素存储在shape对象中
//            cout << "number of parts: " << shape.num_parts() << endl;
            // 这里只输出了shape中的前两个特征点像素的位置
//            cout << "pixel position of first part:  " << shape.part(0) << endl;
//            cout << "pixel position of second part: " << shape.part(1) << endl;
            // put them on the screen.
            // 因此，你能够获取一个人脸所有特征点的像素位置，这些像素点位置存储在shape对象中
            // 图片中所有人脸特征点的像素位置存储在shapes对象中
            shapes.push_back(shape);
        }
        //render to mat
        dst = img.clone();
        renderToMat(shapes, dst);
    }catch (exception& e)
    {
        cout << endl << e.what() << endl;
    }

}

//把所有的特征点用线连接起来
inline void renderToMat(std::vector<full_object_detection>& dets, Mat& dst){
    Scalar color;
    int sz=3;
    //用绿线连接
    color = Scalar(0, 255, 0);
    for (unsigned long idx = 0; idx < dets.size(); ++idx){
        // Around Chin. Ear to Ear
        for (unsigned long i = 1; i <= 16; ++i)
            cv::line(dst, Point(dets[idx].part(i).x(), dets[idx].part(i).y()),
                     Point(dets[idx].part(i-1).x(), dets[idx].part(i-1).y()),
                     color, sz);
        // Line on top of nose
        for (unsigned long i = 28; i <= 30; ++i)
            cv::line(dst, Point(dets[idx].part(i).x(), dets[idx].part(i).y()),
                     Point(dets[idx].part(i-1).x(), dets[idx].part(i-1).y()),
                     color, sz);
        // left eyebrow
        for (unsigned long i = 18; i <= 21; ++i)
            cv::line(dst, Point(dets[idx].part(i).x(), dets[idx].part(i).y()),
                     Point(dets[idx].part(i-1).x(), dets[idx].part(i-1).y()),
                     color, sz);
        // Right eyebrow
        for (unsigned long i = 23; i <= 26; ++i)
            cv::line(dst, Point(dets[idx].part(i).x(), dets[idx].part(i).y()),
                     Point(dets[idx].part(i-1).x(), dets[idx].part(i-1).y()),
                     color, sz);
        // Bottom part of the nose
        for (unsigned long i = 31; i <= 35; ++i)
            cv::line(dst, Point(dets[idx].part(i).x(), dets[idx].part(i).y()),
                     Point(dets[idx].part(i-1).x(), dets[idx].part(i-1).y()),
                     color, sz);
        cv::line(dst, Point(dets[idx].part(30).x(), dets[idx].part(30).y()),
                 Point(dets[idx].part(35).x(), dets[idx].part(35).y()),
                 color, sz);
        // Left eye
        for (unsigned long i = 37; i <= 41; ++i)
            cv::line(dst, Point(dets[idx].part(i).x(), dets[idx].part(i).y()),
                     Point(dets[idx].part(i-1).x(), dets[idx].part(i-1).y()),
                     color, sz);
        cv::line(dst, Point(dets[idx].part(36).x(), dets[idx].part(36).y()),
                 Point(dets[idx].part(41).x(), dets[idx].part(41).y()),
                 color, sz);
        // Right eye
        for (unsigned long i = 43; i <= 47; ++i)
            cv::line(dst, Point(dets[idx].part(i).x(), dets[idx].part(i).y()),
                     Point(dets[idx].part(i-1).x(), dets[idx].part(i-1).y()),
                     color, sz);
        cv::line(dst, Point(dets[idx].part(42).x(), dets[idx].part(42).y()),
                 Point(dets[idx].part(47).x(), dets[idx].part(47).y()),
                 color, sz);
        // Lips outer part
        for (unsigned long i = 49; i <= 59; ++i)
            cv::line(dst, Point(dets[idx].part(i).x(), dets[idx].part(i).y()),
                     Point(dets[idx].part(i-1).x(), dets[idx].part(i-1).y()),
                     color, sz);
        cv::line(dst, Point(dets[idx].part(48).x(), dets[idx].part(48).y()),
                 Point(dets[idx].part(59).x(), dets[idx].part(59).y()),
                 color, sz);
        // Lips inside part
        for (unsigned long i = 61; i <= 67; ++i)
            cv::line(dst, Point(dets[idx].part(i).x(), dets[idx].part(i).y()),
                     Point(dets[idx].part(i-1).x(), dets[idx].part(i-1).y()),
                     color, sz);
        cv::line(dst, Point(dets[idx].part(60).x(), dets[idx].part(60).y()),
                 Point(dets[idx].part(67).x(), dets[idx].part(67).y()),
                 color, sz);
    }
}


void cannyDetection(Mat& img, Mat& dst){
    LOG(INFO) << "in cannyDetection()";
    Mat gray, edge;
	//2.1 创建与srcImage相同大小和类型的dst
	dst.create(img.size(), img.type());
	//2.2 转换为灰度图
	cvtColor(img, gray, COLOR_BGR2GRAY);
	//2.3降噪:3X3的内核
	blur(gray, edge,Size(3,3));
	//2.4调用Canny
	Canny(edge, edge, 3, 9, 3);
	//2.5将dst设置为全0
	dst = Scalar::all(0);
	//2.6 将得到的边缘用作掩码，拷贝原图到效果图上
	img.copyTo(dst, edge);
}
