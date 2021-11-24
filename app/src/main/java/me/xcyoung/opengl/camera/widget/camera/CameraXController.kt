package me.xcyoung.opengl.camera.widget.camera

import android.content.Context
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

/**
 * @author ChorYeung
 * @since 2021/11/24
 */
class CameraXController {
    fun setUpCamera(context: Context, surfaceProvider: Preview.SurfaceProvider) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview: Preview = Preview.Builder().build()

            val imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setTargetResolution(Size(1080, 1440))
//                .setTargetRotation(this.display.rotation)
                .build()

            preview.setSurfaceProvider(surfaceProvider)

            cameraProvider.unbindAll()

            val camera =
                cameraProvider.bindToLifecycle(
                    context as LifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    imageCapture,
                    preview
                )
            val cameraInfo = camera.cameraInfo
            val cameraControl = camera.cameraControl
        }, ContextCompat.getMainExecutor(context))
    }
}