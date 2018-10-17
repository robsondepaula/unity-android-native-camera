# unity-android-native-camera

On this repository you'll find an experiment to draw the Native Android Camera feed onto a Unity GameObject.

The inspiration comes from [Unity Technologies NativeRenderingPlugin](https://bitbucket.org/Unity-Technologies/graphicsdemos/src/364ac57cea5c197ca9b7015ba29dcc1ff94c9f61/NativeRenderingPlugin/).

The folder structure contains the following code:

1 [An NDK library as the low level rendering plugin](NativeCameraPlugin)

2 [An Android Studio project to create and AAR library as Unity Native Plugin](UnityAndroidCameraPlugin)

3 [Finally, the Unity project to illustrate all the plumbing](UnityAndroidCamera)

Upon success you'll see something like the below image on your Android device:
![Screenshot](screen.png)
