#!/bin/bash

NDK_ROOT="/Users/robson.p/Library/Android/sdk/ndk/21.3.6528147"
cd "jni"
$NDK_ROOT/ndk-build -B V=1
cd ..
cp -R libs/armeabi-v7a/libNativeCameraPlugin.so ../UnityAndroidCamera/Assets/Plugins/Android/libs/armeabi-v7a/libNativeCameraPlugin.so
