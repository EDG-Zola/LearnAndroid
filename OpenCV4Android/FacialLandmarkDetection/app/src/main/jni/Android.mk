LOCAL_PATH := $(call my-dir)

#build dlib to static library
include $(CLEAR_VARS)
LOCAL_MODULE := dlib
LOCAL



#opencv
OPENCVROOT:= C:\OpenCV-2.4.9-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
include ${OPENCVROOT}/sdk/native/jni/OpenCV.mk

LOCAL_SRC_FILES := com_inha_vision_ndktest_OpencvNativeClass.cpp

LOCAL_LDLIBS += -llog
LOCAL_MODULE := MyLibs


include $(BUILD_SHARED_LIBRARY)
