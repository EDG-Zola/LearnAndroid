#include <jni.h>
#include <string>

JNIEXPORT jint JNICALL
Java_com_example_cvlab_ndkopencvtest1_OpencvNativeClass_convertGray(JNIEnv *env, jclass type,
                                                                    jlong matAddrRgb,
                                                                    jlong matAddrGray) {

    // TODO

}

extern "C"
jstring
Java_com_example_cvlab_ndkopencvtest1_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
