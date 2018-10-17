LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := NativeCameraPlugin
LOCAL_SRC_FILES := src/RenderingPlugin.cpp
LOCAL_LDLIBS += -llog -ldl -lGLESv2
LOCAL_ARM_MODE := arm
LOCAL_CFLAGS := -DUNITY_ANDROID


include $(BUILD_SHARED_LIBRARY)
