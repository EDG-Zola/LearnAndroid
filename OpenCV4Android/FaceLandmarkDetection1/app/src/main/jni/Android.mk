LOCAL_PATH := $(call my-dir)

#build dlib to static library
include $(CLEAR_VARS)
LOCAL_MODULE := dlib
#LOCAL_PATH is jni/
LOCAL_C_INCLUDES := $(LOCAL_PATH)/dlib
LOCAL_SRC_FILES += \
                ../$(LOCAL_PATH)/../dlib/dlib/threads/threads_kernel_shared.cpp \
                ../$(LOCAL_PATH)/../dlib/dlib/entropy_decoder/entropy_decoder_kernel_2.cpp \
                ../$(LOCAL_PATH)/../dlib/dlib/base64/base64_kernel_1.cpp \
                ../$(LOCAL_PATH)/../dlib/dlib/threads/threads_kernel_1.cpp \
                ../$(LOCAL_PATH)/../dlib/dlib/threads/threads_kernel_2.cpp

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_C_INCLUDES)
include $(BUILD_STATIC_LIBRARY)


#build opencv
TOP_LEVEL_PATH := $(abspath $(LOCAL_PATH)/..)
$(info TOP Level Path: $(TOP_LEVEL_PATH))

EXT_INSTALL_PATH = $(TOP_LEVEL_PATH)/third_party

OPENCV_PATH = $(EXT_INSTALL_PATH)/opencv/jni
OPENCV_INCLUDE_DIR = $(OPENCV_PATH)/include

include $(CLEAR_VARS)
OPENCV_CAMERA_MODULES := off
OPENCV_INSTALL_MODULES := on
OPENCV_LIB_TYPE := STATIC
include $(OPENCV_PATH)/OpenCV.mk


#opencv
#OPENCVROOT:= G:\OpenCV-android-sdk
#OPENCV_CAMERA_MODULES:=on
#OPENCV_INSTALL_MODULES:=on
#OPENCV_LIB_TYPE:=SHARED
#include ${OPENCVROOT}/sdk/native/jni/OpenCV.mk

#import myLibs
LOCAL_MODULE := MyLibs
#LOCAL_C_INCLUDES += $(OPENCV_INCLUDE_DIR)
LOCAL_SRC_FILES := com_example_cvlab_facelandmarkdetection1_NativeClass.cpp
LOCAL_LDLIBS += -lm -llog -ldl -lz -ljnigraphics
LOCAL_CPPFLAGS += -std=c++11 -frtti -fexceptions
LOCAL_STATIC_LIBRARIES += dlib

include $(BUILD_SHARED_LIBRARY)
