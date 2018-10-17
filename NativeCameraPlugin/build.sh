#!/bin/bash

NDK_ROOT="/Developer/android-ndk-r13b"
cd "jni"
$NDK_ROOT/ndk-build -B V=1
cd ..
cp -R libs/armeabi-v7a/libNativeCameraPlugin.so ../UnityAndroidCamera/Assets/Plugins/Android/libs/armeabi-v7a/libNativeCameraPlugin.so
