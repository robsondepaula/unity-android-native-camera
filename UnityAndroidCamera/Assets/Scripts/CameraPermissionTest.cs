using UnityEngine;
#if PLATFORM_ANDROID
using UnityEngine.Android;
#endif

public class CameraPermissionTest : MonoBehaviour
{
    GameObject dialog = null;
    
    void Start ()
    {
        #if PLATFORM_ANDROID
        if (!Permission.HasUserAuthorizedPermission(Permission.Camera))
        {
            Permission.RequestUserPermission(Permission.Camera);
            dialog = new GameObject();
        }
        #endif
    }

    void OnGUI ()
    {
        #if PLATFORM_ANDROID
        if (!Permission.HasUserAuthorizedPermission(Permission.Camera))
        {
            // The user denied permission to use the camera.
            // Display a message explaining why you need it with Yes/No buttons.
            // If the user says yes then present the request again
            // Display a dialog here.
            dialog.AddComponent<PermissionsRationaleDialog>();
            return;
        }
        else if (dialog != null)
        {
            Destroy(dialog);
        }
        #endif
    }
}
