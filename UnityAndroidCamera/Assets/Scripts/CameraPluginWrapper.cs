using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using UnityEngine;

public class CameraPluginWrapper : MonoBehaviour {
    [DllImport ("NativeCameraPlugin")]
    private static extern void SetTextureFromUnity (System.IntPtr texture);

    [DllImport ("NativeCameraPlugin")]
    private static extern IntPtr GetRenderEventFunc ();

    public Material displayMaterial;

    private AndroidJavaObject _androidJavaPlugin = null;

    IEnumerator Start () {
        if (Application.platform == RuntimePlatform.Android) {
            using (AndroidJavaClass javaClass = new AndroidJavaClass ("arp.camera.CameraPluginActivity")) {
                _androidJavaPlugin = javaClass.GetStatic<AndroidJavaObject> ("_context");
            }

            CreateTextureAndPassToPlugin ();
            yield return StartCoroutine ("CallPluginAtEndOfFrames");
        }
    }

    private void CreateTextureAndPassToPlugin () {
        Texture2D tex = new Texture2D (640, 480, TextureFormat.RGBA32, false);
        // Set point filtering just so we can see the pixels clearly
        tex.filterMode = FilterMode.Point;
        // Call Apply() so it's actually uploaded to the GPU
        tex.Apply ();

        displayMaterial.mainTexture = tex;

        SetTextureFromUnity (tex.GetNativeTexturePtr ());

        EnablePreview (true);
    }

    private IEnumerator CallPluginAtEndOfFrames () {
        while (true) {
            // Wait until all frame rendering is done
            yield return new WaitForEndOfFrame ();

            // Issue a plugin event with arbitrary integer identifier.
            // The plugin can distinguish between different
            // things it needs to do based on this ID.
            // For our simple plugin, it does not matter which ID we pass here.
            GL.IssuePluginEvent (GetRenderEventFunc (), 1);

            // skip one frame
            yield return new WaitForEndOfFrame ();
        }
    }

    public void EnablePreview (bool enable) {
        if (_androidJavaPlugin != null) {
            _androidJavaPlugin.Call ("enablePreviewUpdater", enable);
        }
    }

}