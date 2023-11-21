package pro.gid.sdk

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.MultiDex
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableApkTooOldException
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException
import com.google.ar.core.exceptions.UnavailableSdkTooOldException
import com.nixbyte.kmm_architecture.android.common.helpers.ARCoreSessionLifecycleHelper
import org.tensorflow.lite.task.vision.detector.Detection
import pro.gid.sdk.common.helpers.CameraPermissionHelper
import pro.gid.sdk.common.helpers.DepthSettings
import pro.gid.sdk.common.helpers.FullScreenHelper
import pro.gid.sdk.common.helpers.InstantPlacementSettings
import pro.gid.sdk.common.samplerender.SampleRender
import pro.gid.sdk.object_detection.ObjectDetectorHelper

/**
 * This is a simple example that shows how to create an augmented reality (AR) application using the
 * ARCore API. The application will display any detected planes and will allow the user to tap on a
 * plane to place a 3D model.
 */
class LahtaActivity : AppCompatActivity(), ObjectDetectorHelper.DetectorListener {
    companion object {
        private const val TAG = "HelloArActivity"
    }

    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
    lateinit var view: HelloArView
    lateinit var renderer: HelloArRenderer

    val instantPlacementSettings =
        InstantPlacementSettings()
    val depthSettings = DepthSettings()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
        // Setup ARCore session lifecycle helper and configuration.
//        arCoreSessionHelper = ARCoreSessionLifecycleHelper(this)
//        // If Session creation or Session.resume() fails, display a message and log detailed
//        // information.
//        arCoreSessionHelper.exceptionCallback =
//            { exception ->
//                val message =
//                    when (exception) {
//                        is UnavailableUserDeclinedInstallationException ->
//                            "Please install Google Play Services for AR"
//                        is UnavailableApkTooOldException -> "Please update ARCore"
//                        is UnavailableSdkTooOldException -> "Please update this app"
//                        is UnavailableDeviceNotCompatibleException -> "This device does not support AR"
//                        is CameraNotAvailableException -> "Camera not available. Try restarting the app."
//                        else -> "Failed to create AR session: $exception"
//                    }
//                Log.e(TAG, "ARCore threw an exception", exception)
//                view.snackbarHelper.showError(this, message)
//            }
//
//        // Configure session features, including: Lighting Estimation, Depth mode, Instant Placement.
//        arCoreSessionHelper.beforeSessionResume = ::configureSession
//        lifecycle.addObserver(arCoreSessionHelper)
//
//        // Set up the Hello AR renderer.
//        renderer = HelloArRenderer(this)
//        lifecycle.addObserver(renderer)
//
//        // Set up Hello AR UI.
//        view = HelloArView(this)
//        lifecycle.addObserver(view)
//        setContentView(view.root)
//
//        // Sets up an example renderer using our HelloARRenderer.
//        SampleRender(view.surfaceView, renderer, assets)
//
//        depthSettings.onCreate(this)
//        instantPlacementSettings.onCreate(this)
    }

    // Configure the session, using Lighting Estimation, and Depth mode.
    fun configureSession(session: Session) {
        session.configure(
            session.config.apply {
                lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR

                // Depth API is used if it is configured in Hello AR's settings.
                depthMode =
                    if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        Config.DepthMode.AUTOMATIC
                    } else {
                        Config.DepthMode.DISABLED
                    }

                // Instant Placement is used if it is configured in Hello AR's settings.
                instantPlacementMode =
                    if (instantPlacementSettings.isInstantPlacementEnabled) {
                        Config.InstantPlacementMode.LOCAL_Y_UP
                    } else {
                        Config.InstantPlacementMode.DISABLED
                    }
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            // Use toast instead of snackbar here since the activity will exit.
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                .show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this)
            }
            finish()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }

    override fun onError(error: String) {
        Log.e("onError", "$error")
    }

    override fun onResults(
        results: MutableList<Detection>?,
        inferenceTime: Long,
        imageHeight: Int,
        imageWidth: Int
    ) {
        Log.e("Results", "${results?.size}")
    }
}