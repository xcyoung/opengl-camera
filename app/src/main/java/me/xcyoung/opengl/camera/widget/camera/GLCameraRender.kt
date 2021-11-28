package me.xcyoung.opengl.camera.widget.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Size
import android.view.Surface
import androidx.annotation.WorkerThread
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import java.util.concurrent.Executors
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author ChorYeung
 * @since 2021/11/24
 */
class GLCameraRender(private val context: Context, private val callback: Callback) : GLSurfaceView.Renderer,
    Preview.SurfaceProvider, SurfaceTexture.OnFrameAvailableListener {
    private var textures: IntArray = IntArray(1)
    private var surfaceTexture: SurfaceTexture? = null
    private var textureMatrix: FloatArray = FloatArray(16)
    private val executor = Executors.newSingleThreadExecutor()
    private var filter: Filter? = null
    var type: String = "Normal"

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        gl?.let {
            it.glGenTextures(textures.size, textures, 0)
            surfaceTexture = SurfaceTexture(textures[0])
            filter = when(type) {
                "WhiteBalance" -> WhiteBalanceFilter(context)
                else -> ScreenFilter(context)
            }
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        callback.onSurfaceChanged()
        filter?.onReady(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        val surfaceTexture = this.surfaceTexture
        if (gl == null || surfaceTexture == null) return
        gl.glClearColor(0f, 0f, 0f, 0f)
        gl.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        surfaceTexture.updateTexImage()
        surfaceTexture.getTransformMatrix(textureMatrix)
        filter?.setTransformMatrix(textureMatrix)
        filter?.onDrawFrame(textures[0])
    }

    override fun onSurfaceRequested(request: SurfaceRequest) {
        val resetTexture = resetPreviewTexture(request.resolution) ?: return
        val surface = Surface(resetTexture)
        request.provideSurface(surface, executor) {
            surface.release()
            surfaceTexture?.release()
        }
    }

    @WorkerThread
    private fun resetPreviewTexture(size: Size): SurfaceTexture? {
        return this.surfaceTexture?.let { surfaceTexture ->
            surfaceTexture.setOnFrameAvailableListener(this)
            surfaceTexture.setDefaultBufferSize(size.width, size.height)
            surfaceTexture
        }
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        callback.onFrameAvailable()
    }

    fun setProgress(progress: Float) {
        if (filter is WhiteBalanceFilter) {
            (filter as WhiteBalanceFilter).temperature = progress
        }
    }

    fun setTint(progress: Float) {
        if (filter is WhiteBalanceFilter) {
            (filter as WhiteBalanceFilter).tint = progress
        }
    }

    interface Callback {
        fun onSurfaceChanged()
        fun onFrameAvailable()
    }
}